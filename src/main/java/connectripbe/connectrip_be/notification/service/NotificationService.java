package connectripbe.connectrip_be.notification.service;

import connectripbe.connectrip_be.member.entity.MemberEntity;
import java.util.List;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {

    SseEmitter subscribe(Long memberId); // SSE 연결 구독


    void markAsRead(Long notificationId); // 알림 읽음 처리

    List<Object> getUnreadNotifications(Long memberId);

    void sendNotification(Long memberId, Object post, String content, MemberEntity member); // 알림 전송
}
