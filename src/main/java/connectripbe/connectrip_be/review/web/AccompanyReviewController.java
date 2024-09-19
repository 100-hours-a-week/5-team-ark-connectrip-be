package connectripbe.connectrip_be.review.web;

import connectripbe.connectrip_be.review.dto.AccompanyReviewRequest;
import connectripbe.connectrip_be.review.dto.AccompanyReviewResponse;
import connectripbe.connectrip_be.review.service.AccompanyReviewService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chatrooms/{chatRoomId}/reviews")
@RequiredArgsConstructor
public class AccompanyReviewController {

    private final AccompanyReviewService accompanyReviewService;

    /**
     * 특정 채팅방에 대한 리뷰 생성. 주어진 채팅방 ID와 리뷰 생성 요청 정보를 기반으로 리뷰를 생성합니다. FINISH 상태의 채팅방에 대해서만 리뷰 작성이 가능합니다.
     *
     * @param chatRoomId    리뷰를 작성할 채팅방의 ID
     * @param reviewRequest 리뷰 생성 요청 정보
     * @return 생성된 리뷰 정보를 담은 ResponseEntity<AccompanyReviewResponse>
     */
    @PostMapping
    public ResponseEntity<AccompanyReviewResponse> createReview(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long chatRoomId,
            @RequestBody AccompanyReviewRequest reviewRequest) {
        AccompanyReviewResponse response =
                accompanyReviewService.createReview(chatRoomId, memberId, reviewRequest);

        return ResponseEntity.ok(response);
    }

    /**
     * 특정 채팅방에 달린 모든 리뷰 조회. 주어진 채팅방 ID에 해당하는 모든 리뷰를 조회하여 반환합니다.
     *
     * @param chatRoomId 리뷰를 조회할 채팅방의 ID
     * @return 채팅방에 달린 리뷰 목록을 담은 ResponseEntity<List<AccompanyReviewResponse>>
     */
    @GetMapping
    public ResponseEntity<List<AccompanyReviewResponse>> getReviewsByChatRoom(@PathVariable Long chatRoomId) {
        List<AccompanyReviewResponse> responses = accompanyReviewService.getReviewsByChatRoomId(chatRoomId);
        return ResponseEntity.ok(responses);
    }
}