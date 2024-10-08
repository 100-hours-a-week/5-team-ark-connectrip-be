package connectripbe.connectrip_be.notification.service.impl;

import connectripbe.connectrip_be.accompany.post.entity.AccompanyPostEntity;
import connectripbe.connectrip_be.community.post.entity.CommunityPostEntity;
import connectripbe.connectrip_be.global.exception.GlobalException;
import connectripbe.connectrip_be.global.exception.type.ErrorCode;
import connectripbe.connectrip_be.member.entity.MemberEntity;
import connectripbe.connectrip_be.member.repository.MemberJpaRepository;
import connectripbe.connectrip_be.notification.dto.NotificationCommentResponse;
import connectripbe.connectrip_be.notification.dto.NotificationCommunityCommentResponse;
import connectripbe.connectrip_be.notification.entity.NotificationEntity;
import connectripbe.connectrip_be.notification.repository.NotificationRepository;
import connectripbe.connectrip_be.notification.service.NotificationService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
     * 통합된 알림 전송 메서드. 동행 게시물 또는 커뮤니티 게시물의 알림을 처리하며, 게시물 타입에 따라 해당 게시물 필드 (accompany_post_id 또는 community_post_id)를
     * 설정합니다. 두 필드는 동시에 설정되지 않도록 보장됩니다.
     *
     * @param memberId       알림을 받을 사용자의 ID
     * @param post           알림이 발생한 게시물 (동행 게시물 또는 커뮤니티 게시물)
     * @param commentContent 댓글 내용 (최대 20자로 제한되어 전송됩니다)
     * @param commentAuthor  댓글 작성자의 정보
     */
    @Override
    public void sendNotification(Long memberId, Object post, String commentContent, MemberEntity commentAuthor) {
        // 사용자 ID로 MemberEntity 조회
        MemberEntity member = memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));

        // 댓글 내용을 20자로 제한
        String limitedContent = limitContentTo20Characters(commentContent);

        // NotificationEntity 빌더를 사용하여 알림 생성 (게시물 타입에 따라 하나의 필드만 설정)
        NotificationEntity notification;

        if (post instanceof AccompanyPostEntity) {
            // 동행 게시물 알림일 경우 communityPostEntity는 null이어야 함
            notification = NotificationEntity.builder()
                    .member(member)
                    .accompanyPostEntity((AccompanyPostEntity) post)  // 동행 게시물 설정
                    .message(limitedContent)
                    .build();
        } else if (post instanceof CommunityPostEntity) {
            // 커뮤니티 게시물 알림일 경우 accompanyPostEntity는 null이어야 함
            notification = NotificationEntity.builder()
                    .member(member)
                    .communityPostEntity((CommunityPostEntity) post)  // 커뮤니티 게시물 설정
                    .message(limitedContent)
                    .build();
        } else {
            throw new GlobalException(ErrorCode.UNSUPPORTED_POST_TYPE);
        }

        // 알림 저장
        notificationRepository.save(notification);

// SSE를 통해 실시간 알림 전송
        SseEmitter emitter = emitters.get(memberId);
        if (emitter != null) {
            try {
                if (post instanceof AccompanyPostEntity) {
                    // 동행 게시물 알림 응답 전송
                    NotificationCommentResponse notificationResponse = NotificationCommentResponse.fromNotification(
                            notification, limitedContent);
                    emitter.send(SseEmitter.event().name("COMMENT_ADDED").data(notificationResponse));
                } else {
                    // 커뮤니티 게시물 알림 응답 전송 (post가 항상 CommunityPostEntity일 경우)
                    NotificationCommunityCommentResponse notificationResponse = NotificationCommunityCommentResponse.fromNotification(
                            notification, limitedContent);
                    emitter.send(SseEmitter.event().name("COMMENT_ADDED").data(notificationResponse));
                }
            } catch (IOException e) {
                emitters.remove(memberId);  // 전송 실패 시 구독 해제
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
     * 주어진 회원 ID를 이용해 읽지 않은 알림을 조회하는 메서드. 알림이 동행 게시물 또는 커뮤니티 게시물과 연관된 경우 각각 다른 DTO로 변환하여 반환합니다. 조회된 알림 메시지는 20자 이하로
     * 제한됩니다.
     *
     * @param memberId 읽지 않은 알림을 조회할 회원의 ID
     * @return 읽지 않은 알림 목록을 NotificationCommentResponse 또는 NotificationCommunityCommentResponse 객체 리스트로 반환
     */
    @Override
    @Transactional(readOnly = true)
    public List<Object> getUnreadNotifications(Long memberId) {
        MemberEntity member = memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));

        // 읽지 않은 알림 조회 (readAt이 null인 알림만 조회)
        List<NotificationEntity> unreadNotifications = notificationRepository.findAllByMemberAndReadAtIsNull(member);

        if (unreadNotifications.isEmpty()) {
            return Collections.emptyList();
        }

        // 메서드 참조로 변환
        return unreadNotifications.stream()
                .map(this::convertNotificationToDTO) // 메서드 참조로 변경
                .collect(Collectors.toList());
    }

    /**
     * NotificationEntity를 적절한 DTO로 변환하는 메서드. 알림이 동행 게시물 또는 커뮤니티 게시물과 연관된 경우 각각 NotificationCommentResponse 또는
     * NotificationCommunityCommentResponse로 변환하여 반환합니다.
     *
     * @param notification 변환할 NotificationEntity 객체
     * @return 변환된 DTO 객체 (NotificationCommentResponse 또는 NotificationCommunityCommentResponse)
     */
    private Object convertNotificationToDTO(NotificationEntity notification) {
        if (notification.getAccompanyPostEntity() != null) {
            // 동행 게시물 알림 응답 객체로 변환
            return NotificationCommentResponse.fromNotification(notification, notification.getMessage());
        } else if (notification.getCommunityPostEntity() != null) {
            // 커뮤니티 게시물 알림 응답 객체로 변환
            return NotificationCommunityCommentResponse.fromNotification(notification, notification.getMessage());
        } else {
            // 지원되지 않는 게시물 타입일 경우 예외 발생
            throw new GlobalException(ErrorCode.UNSUPPORTED_POST_TYPE);
        }
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
