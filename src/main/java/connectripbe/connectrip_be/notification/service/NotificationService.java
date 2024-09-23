package connectripbe.connectrip_be.notification.service;

import connectripbe.connectrip_be.notification.dto.NotificationCommentResponse;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {

    SseEmitter subscribe(Long memberId); // SSE 연결 구독

    void sendNotification(Long memberId, NotificationCommentResponse notificationResponse); // 알림 전송
}
