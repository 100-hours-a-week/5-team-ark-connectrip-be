package connectripbe.connectrip_be.commentnotification.web;

import connectripbe.connectrip_be.commentnotification.entity.CommentNotificationEntity;
import connectripbe.connectrip_be.commentnotification.service.CommentNotificationService;
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
public class CommentNotificationController {

    private final CommentNotificationService notificationService;

    @GetMapping("/subscribe/{memberId}")
    public SseEmitter subscribe(@PathVariable Long memberId) {
        return notificationService.subscribe(memberId);
    }

    @GetMapping("/unread/{memberId}")
    public ResponseEntity<List<CommentNotificationEntity>> getUnreadNotifications(@PathVariable Long memberId) {
        List<CommentNotificationEntity> notifications = notificationService.getUnreadNotifications(memberId);
        return ResponseEntity.ok(notifications);
    }
}
