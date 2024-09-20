package connectripbe.connectrip_be.commentnotification.service;

import connectripbe.connectrip_be.commentnotification.entity.CommentNotificationEntity;
import java.util.List;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface CommentNotificationService {

    SseEmitter subscribe(Long memberId); // SSE 연결 구독

    void sendNotification(Long memberId, String message); // 알림 전송

    List<CommentNotificationEntity> getUnreadNotifications(Long memberId); // 읽지 않은 알림 조회
}
