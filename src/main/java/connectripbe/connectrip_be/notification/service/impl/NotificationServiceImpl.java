package connectripbe.connectrip_be.notification.service.impl;

import connectripbe.connectrip_be.member.entity.MemberEntity;
import connectripbe.connectrip_be.notification.dto.NotificationCommentResponse;
import connectripbe.connectrip_be.notification.entity.NotificationEntity;
import connectripbe.connectrip_be.notification.repository.NotificationRepository;
import connectripbe.connectrip_be.notification.service.NotificationService;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    /**
     * 사용자가 알림을 구독할 수 있도록 SSE 연결을 설정하는 메서드. SSE 연결을 통해 실시간 알림을 구독하며, 연결이 완료되면 "알림 연결 완료" 메시지를 전송합니다.
     *
     * @param memberId 알림을 구독할 사용자의 ID
     * @return SSE Emitter 객체 (알림 구독을 위한 연결)
     */
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

    /**
     * 특정 사용자에게 알림을 전송하는 메서드. NotificationCommentResponse를 기반으로 알림 데이터를 저장하고, SSE로 실시간 알림을 전송합니다.
     *
     * @param memberId             알림을 받을 사용자의 ID
     * @param notificationResponse 전송할 알림 정보
     */
    @Override
    public void sendNotification(Long memberId, NotificationCommentResponse notificationResponse) {
        NotificationEntity notification = NotificationEntity.builder()
                .member(MemberEntity.builder().id(memberId).build())
                .message(notificationResponse.getContent())  // 알림 메시지는 댓글 내용
                .build();
        notificationRepository.save(notification);

        SseEmitter emitter = emitters.get(memberId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("COMMENT_ADDED")
                        .data(notificationResponse));  // NotificationCommentResponse 객체 전송
            } catch (IOException e) {
                emitters.remove(memberId);
            }
        }
    }

    /**
     * 특정 사용자의 읽지 않은 알림 목록을 조회하는 메서드. 읽지 않은 알림(읽음 시간이 설정되지 않은 알림)을 데이터베이스에서 조회하여 반환합니다.
     *
     * @param memberId 알림을 조회할 사용자의 ID
     * @return 읽지 않은 알림 목록
     */
    @Override
    public List<NotificationEntity> getUnreadNotifications(Long memberId) {
        return notificationRepository.findByMemberIdAndReadAtIsNull(memberId);
    }
}
