package connectripbe.connectrip_be.pending_list.web;

import connectripbe.connectrip_be.auth.config.LoginUser;
import connectripbe.connectrip_be.pending_list.dto.PendingListResponse;
import connectripbe.connectrip_be.pending_list.service.PendingListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/accompany/posts/{id}/pending")
@RequiredArgsConstructor
public class PendingListController {

    private final PendingListService pendingListService;

    // 동행 신청 상태 조회
    @GetMapping
    public ResponseEntity<PendingListResponse> getMyPendingStatus(@PathVariable Long id,
            @LoginUser String email) {
        return ResponseEntity.ok(pendingListService.getMyPendingStatus(id, email));
    }

    // 동행 신청
    @PostMapping
    public ResponseEntity<PendingListResponse> accompanyPending(@PathVariable Long id,
            @LoginUser String email) {
        return ResponseEntity.ok(pendingListService.accompanyPending(id, email));
    }
}