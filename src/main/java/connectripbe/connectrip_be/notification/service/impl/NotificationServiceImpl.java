package connectripbe.connectrip_be.notification.service.impl;

import connectripbe.connectrip_be.global.exception.GlobalException;
import connectripbe.connectrip_be.global.exception.type.ErrorCode;
import connectripbe.connectrip_be.member.entity.MemberEntity;
import connectripbe.connectrip_be.member.repository.MemberJpaRepository;
import connectripbe.connectrip_be.notification.dto.NotificationCommentResponse;
import connectripbe.connectrip_be.notification.entity.NotificationEntity;
import connectripbe.connectrip_be.notification.repository.NotificationRepository;
import connectripbe.connectrip_be.notification.service.NotificationService;
import connectripbe.connectrip_be.post.entity.AccompanyPostEntity;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final MemberJpaRepository memberJpaRepository;
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
     * 특정 사용자가 알림을 받을 때, 해당 사용자의 SSE 구독이 활성화되어 있으면 실시간으로 알림을 전송하는 메서드.
     *
     * @param memberId             알림을 받을 사용자의 ID
     * @param notificationResponse 알림 내용
     */
    @Override
    public void sendNotification(Long memberId, AccompanyPostEntity post,
                                 NotificationCommentResponse notificationResponse) {

        // 사용자 ID로 MemberEntity 조회
        MemberEntity member = memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));

        // 댓글 내용을 20자로 제한
        String limitedContent = limitContentTo20Characters(notificationResponse.getContent());

        // NotificationEntity를 빌더 패턴으로 생성
        NotificationEntity notification = NotificationEntity.builder()
                .member(member)
                .accompanyPostEntity(post)
                .message(limitedContent)  // 제한된 알림 메시지를 사용
                .build();

        notificationRepository.save(notification);

        // 알림을 받을 사용자에게 실시간 알림 전송
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
     * 알림 읽음 처리 메서드. 주어진 알림 ID를 이용해 해당 알림을 찾아 'readAt' 필드를 현재 시간으로 업데이트하여 읽음 처리합니다.
     *
     * @param notificationId 읽음 처리할 알림의 ID
     */
    @Override
    public void markAsRead(Long notificationId) {
        NotificationEntity notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOTIFICATION_NOT_FOUND));

        // 알림의 읽음 처리 (readAt 필드를 업데이트하는 메서드 호출)
        notification.markAsRead(LocalDateTime.now());

        notificationRepository.save(notification);
    }

    /**
     * 주어진 회원 ID를 이용해 읽지 않은 알림을 조회하는 메서드. 조회된 알림 메시지는 20자 이하로 제한됩니다.
     *
     * @param memberId 읽지 않은 알림을 조회할 회원의 ID
     * @return 읽지 않은 알림 목록을 NotificationCommentResponse 객체 리스트로 반환
     */
    @Override
    public List<NotificationCommentResponse> getUnreadNotifications(Long memberId) {
        MemberEntity member = memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));

        // 읽지 않은 알림 조회 (readAt이 null인 알림만 조회) 후 바로 변환하여 반환
        return notificationRepository.findAllByMemberAndReadAtIsNull(member).stream()
                .map(this::convertNotificationToResponse)
                .collect(Collectors.toList());
    }

    /**
     * NotificationEntity를 NotificationCommentResponse로 변환하는 메서드
     *
     * @param notification 변환할 NotificationEntity 객체
     * @return NotificationCommentResponse 객체
     */
    private NotificationCommentResponse convertNotificationToResponse(NotificationEntity notification) {
        String limitedContent = limitContentTo20Characters(notification.getMessage());
        return NotificationCommentResponse.fromNotification(notification, limitedContent);
    }


    /**
     * 댓글 내용을 20자 이하로 제한하는 메서드. 댓글 내용이 20자보다 길 경우 첫 20자를 반환하고, 그렇지 않으면 전체 내용을 반환합니다.
     *
     * @param content 제한할 댓글 내용
     * @return 20자 이하로 제한된 댓글 내용
     */
    private String limitContentTo20Characters(String content) {
        return content.length() > 20 ? content.substring(0, 20) : content;
    }


}
