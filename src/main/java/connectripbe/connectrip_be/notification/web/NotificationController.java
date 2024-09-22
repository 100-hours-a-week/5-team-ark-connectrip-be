package connectripbe.connectrip_be.notification.web;

import connectripbe.connectrip_be.notification.entity.NotificationEntity;
import connectripbe.connectrip_be.notification.service.NotificationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1/comment-notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/subscribe/{memberId}")
    public SseEmitter subscribe(@PathVariable Long memberId) {
        return notificationService.subscribe(memberId);
    }

    @GetMapping("/unread/{memberId}")
    public ResponseEntity<List<NotificationEntity>> getUnreadNotifications(@PathVariable Long memberId) {
        List<NotificationEntity> notifications = notificationService.getUnreadNotifications(memberId);
        return ResponseEntity.ok(notifications);
    }
}
