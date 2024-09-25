package connectripbe.connectrip_be.notification.web;

import connectripbe.connectrip_be.notification.service.NotificationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // 알림 구독 및 전송 API
    @GetMapping("/subscribe/{memberId}")
    public SseEmitter subscribe(@PathVariable Long memberId) {
        return notificationService.subscribe(memberId);
    }

    // 알림 읽음 처리 API
    @PostMapping("/read/{notificationId}")
    public ResponseEntity<Void> markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok().build();
    }

    // 읽지 않은 알림 조회 API (동행 댓글과 커뮤니티 댓글 통합)
    @GetMapping("/unread/{memberId}")
    public ResponseEntity<List<Object>> getUnreadNotifications(@PathVariable Long memberId) {
        List<Object> unreadNotifications = notificationService.getUnreadNotifications(memberId);
        return ResponseEntity.ok(unreadNotifications);
    }

}
