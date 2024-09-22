package connectripbe.connectrip_be.notification.service;

import connectripbe.connectrip_be.notification.entity.NotificationEntity;
import java.util.List;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {

    SseEmitter subscribe(Long memberId); // SSE 연결 구독

    void sendNotification(Long memberId, String message); // 알림 전송

    List<NotificationEntity> getUnreadNotifications(Long memberId); // 읽지 않은 알림 조회
}
