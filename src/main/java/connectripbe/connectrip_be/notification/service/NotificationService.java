package connectripbe.connectrip_be.notification.service;

import connectripbe.connectrip_be.notification.dto.NotificationCommentResponse;
import connectripbe.connectrip_be.post.entity.AccompanyPostEntity;
import java.util.List;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {

    SseEmitter subscribe(Long memberId); // SSE 연결 구독

    void sendNotification(Long memberId, AccompanyPostEntity post,
                          NotificationCommentResponse notificationResponse); // 알림 전송

    void markAsRead(Long notificationId); // 알림 읽음 처리

    List<NotificationCommentResponse> getUnreadNotifications(Long memberId); // 읽지 않은 알림 조회

}
