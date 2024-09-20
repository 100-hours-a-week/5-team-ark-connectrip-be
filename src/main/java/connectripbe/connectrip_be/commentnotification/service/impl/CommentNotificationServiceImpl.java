package connectripbe.connectrip_be.commentnotification.service.impl;

import connectripbe.connectrip_be.commentnotification.entity.CommentNotificationEntity;
import connectripbe.connectrip_be.commentnotification.repository.CommentNotificationRepository;
import connectripbe.connectrip_be.commentnotification.service.CommentNotificationService;
import connectripbe.connectrip_be.member.entity.MemberEntity;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class CommentNotificationServiceImpl implements CommentNotificationService {

    private final CommentNotificationRepository notificationRepository;
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    @Override
    public SseEmitter subscribe(Long memberId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(memberId, emitter);

        try {
            emitter.send(SseEmitter.event().name("CONNECTED").data("알림 연결 완료"));
        } catch (IOException e) {
            emitters.remove(memberId);
        }

        emitter.onCompletion(() -> emitters.remove(memberId));
        emitter.onTimeout(() -> emitters.remove(memberId));

        return emitter;
    }

    @Override
    public void sendNotification(Long memberId, String message) {
        CommentNotificationEntity notification = CommentNotificationEntity.builder()
                .member(MemberEntity.builder().id(memberId).build())
                .message(message)
                .build();
        notificationRepository.save(notification);

        SseEmitter emitter = emitters.get(memberId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("COMMENT_ADDED").data(message));
            } catch (IOException e) {
                emitters.remove(memberId);
            }
        }
    }

    @Override
    public List<CommentNotificationEntity> getUnreadNotifications(Long memberId) {
        return notificationRepository.findByMemberIdAndReadAtIsNull(memberId);
    }
}
