package connectripbe.connectrip_be.review.service;

import connectripbe.connectrip_be.review.dto.AccompanyReviewListResponse;
import connectripbe.connectrip_be.review.dto.AccompanyReviewRequest;
import connectripbe.connectrip_be.review.dto.AccompanyReviewResponse;
import connectripbe.connectrip_be.review.dto.AccompanyReviewSummaryResponse;
import java.util.List;

public interface AccompanyReviewService {

    AccompanyReviewResponse createReview(Long chatRoomId, Long memberId, AccompanyReviewRequest reviewRequest);

    List<AccompanyReviewResponse> getReviewsByChatRoomId(Long chatRoomId);

    AccompanyReviewSummaryResponse getReviewSummary(
            Long chatRoomId,
            Long reviewerId,
            Long revieweeId);

    AccompanyReviewListResponse getAllReviews(Long memberId);  // 모든 리뷰 조회

}
