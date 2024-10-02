package connectripbe.connectrip_be.notification.web;

import connectripbe.connectrip_be.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Pending List", description = "Accompany 게시물의 대기자 목록 관리")
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * 알림 구독 및 전송 API. 인증된 사용자의 ID를 사용하여 SSE 연결을 통해 실시간 알림을 구독합니다.
     *
     * @param memberId 인증된 사용자의 ID
     * @return SSE Emitter 객체
     */
    @GetMapping("/subscribe")
    public SseEmitter subscribe(
            @AuthenticationPrincipal Long memberId
    ) {
        return notificationService.subscribe(memberId);
    }

    /**
     * 알림 읽음 처리 API. 인증된 사용자의 알림을 읽음 처리합니다.
     *
     * @param notificationId 읽음 처리할 알림 ID
     * @return 상태 코드 200을 반환
     */
    @PostMapping("/read/{notificationId}")
    public ResponseEntity<Void> markAsRead(
            @PathVariable Long notificationId
    ) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok().build();
    }

    /**
     * 읽지 않은 알림 조회 API. 인증된 사용자의 읽지 않은 알림을 조회하여 반환합니다.
     *
     * @param memberId 인증된 사용자의 ID
     * @return 읽지 않은 알림 목록
     */
    @GetMapping("/unread")
    public ResponseEntity<List<Object>> getUnreadNotifications(
            @AuthenticationPrincipal Long memberId
    ) {
        List<Object> unreadNotifications = notificationService.getUnreadNotifications(memberId);
        return ResponseEntity.ok(unreadNotifications);
    }
}
