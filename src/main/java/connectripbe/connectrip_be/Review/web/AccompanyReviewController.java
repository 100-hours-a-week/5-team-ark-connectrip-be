package connectripbe.connectrip_be.Review.web;

import connectripbe.connectrip_be.Review.dto.AccompanyReviewRequest;
import connectripbe.connectrip_be.Review.dto.AccompanyReviewResponse;
import connectripbe.connectrip_be.Review.service.AccompanyReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accompany/posts/{postId}/reviews")
@RequiredArgsConstructor
public class AccompanyReviewController {

    private final AccompanyReviewService accompanyReviewService;

    @PostMapping
    public ResponseEntity<AccompanyReviewResponse> createReview(
            @PathVariable Long postId,
            @RequestBody AccompanyReviewRequest reviewRequest) {
        AccompanyReviewResponse response = accompanyReviewService.createReview(reviewRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<AccompanyReviewResponse>> getReviewsByPost(@PathVariable Long postId) {
        List<AccompanyReviewResponse> responses = accompanyReviewService.getReviewsByAccompanyPostId(postId);
        return ResponseEntity.ok(responses);
    }
}
