package connectripbe.connectrip_be.pending_list.web;

import connectripbe.connectrip_be.pending_list.dto.PendingListResponse;
import connectripbe.connectrip_be.pending_list.dto.PendingResponse;
import connectripbe.connectrip_be.pending_list.service.PendingListService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<PendingResponse> getMyPendingStatus(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long id) {
        return ResponseEntity.ok(pendingListService.getMyPendingStatus(memberId, id));
    }

    // 동행 신청
    @PostMapping
    public ResponseEntity<PendingResponse> accompanyPending(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(pendingListService.accompanyPending(memberId, id));
    }

    // 신청 내역 리스트 출력
    @GetMapping("/list")
    public ResponseEntity<List<PendingListResponse>> getPendingList(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(pendingListService.getPendingList(memberId, id));
    }
}
