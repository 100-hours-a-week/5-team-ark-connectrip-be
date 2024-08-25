package connectripbe.connectrip_be.Review.web;

import connectripbe.connectrip_be.Review.dto.AccompanyReviewRequest;
import connectripbe.connectrip_be.Review.dto.AccompanyReviewResponse;
import connectripbe.connectrip_be.Review.service.AccompanyReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chatrooms/{chatRoomId}/reviews")  // 수정된 부분
@RequiredArgsConstructor
public class AccompanyReviewController {

    private final AccompanyReviewService accompanyReviewService;

    @PostMapping
    public ResponseEntity<AccompanyReviewResponse> createReview(
            @PathVariable Long chatRoomId,
            @RequestBody AccompanyReviewRequest reviewRequest) {
        AccompanyReviewResponse response = accompanyReviewService.createReview(reviewRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<AccompanyReviewResponse>> getReviewsByChatRoom(@PathVariable Long chatRoomId) {
        List<AccompanyReviewResponse> responses = accompanyReviewService.getReviewsByChatRoomId(chatRoomId);
        return ResponseEntity.ok(responses);
    }
}
