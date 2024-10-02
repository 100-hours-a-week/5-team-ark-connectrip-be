package connectripbe.connectrip_be.review.web;

import connectripbe.connectrip_be.review.dto.AccompanyReviewRequest;
import connectripbe.connectrip_be.review.dto.response.AccompanyReviewListResponse;
import connectripbe.connectrip_be.review.dto.response.AccompanyReviewResponse;
import connectripbe.connectrip_be.review.dto.response.AccompanyReviewSummaryResponse;
import connectripbe.connectrip_be.review.service.AccompanyReviewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@Tag(name = "Accompany Review", description = "Accompany 리뷰 관리")
public class AccompanyReviewController {

    private final AccompanyReviewService accompanyReviewService;

    /**
     * 특정 채팅방에 대한 리뷰 생성. 주어진 채팅방 ID와 리뷰 생성 요청 정보를 기반으로 리뷰를 생성합니다. FINISH 상태의 채팅방에 대해서만 리뷰 작성이 가능합니다.
     *
     * @param chatRoomId    리뷰를 작성할 채팅방의 ID
     * @param reviewRequest 리뷰 생성 요청 정보
     * @return 생성된 리뷰 정보를 담은 ResponseEntity<AccompanyReviewResponse>
     */
    @PostMapping("/{chatRoomId}")
    public ResponseEntity<AccompanyReviewResponse> createReview(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long chatRoomId,
            @RequestBody AccompanyReviewRequest reviewRequest) {
        AccompanyReviewResponse response =
                accompanyReviewService.createReview(chatRoomId, memberId, reviewRequest);

        return ResponseEntity.ok(response);
    }

    /**
     * 회원의 모든 리뷰 조회
     *
     * @param memberId 조회할 회원의 ID
     * @return 회원이 받은 모든 리뷰 목록
     */
    @GetMapping("/profile/{memberId}")
    public ResponseEntity<AccompanyReviewListResponse> getAllReviews(@PathVariable Long memberId) {
        AccompanyReviewListResponse response =
                accompanyReviewService.getAllReviews(memberId);

        return ResponseEntity.ok(response);
    }

//    해당 엔드-포인트 사용하지 않아서 주석 처리
//    /**
//     * 특정 채팅방에 달린 모든 리뷰 조회. 주어진 채팅방 ID에 해당하는 모든 리뷰를 조회하여 반환합니다.
//     *
//     * @param chatRoomId 리뷰를 조회할 채팅방의 ID
//     * @return 채팅방에 달린 리뷰 목록을 담은 ResponseEntity<List<AccompanyReviewResponse>>
//     */
//    @GetMapping
//    public ResponseEntity<List<AccompanyReviewResponse>> getReviewsByChatRoom(@PathVariable Long chatRoomId) {
//        List<AccompanyReviewResponse> responses = accompanyReviewService.getReviewsByChatRoomId(chatRoomId);
//        return ResponseEntity.ok(responses);
//    }

    /**
     * 특정 채팅방 중 리뷰 대상에게 리뷰어가 작성한 리뷰 조회. 주어진 채팅방 ID, 리뷰이 ID, 리뷰어 ID를 조회하여 반환합니다.
     *
     * @return 리뷰
     */
    @GetMapping("/{chatRoomId}")
    public ResponseEntity<AccompanyReviewSummaryResponse> getReviewSummary
    (
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long chatRoomId,
            @RequestParam Long revieweeId
    ) {
        AccompanyReviewSummaryResponse accompanyReviewSummaryResponse =
                accompanyReviewService.getReviewSummary(chatRoomId, memberId, revieweeId);

        return ResponseEntity.ok(accompanyReviewSummaryResponse);
    }
}
