package connectripbe.connectrip_be.review.service;

import connectripbe.connectrip_be.review.dto.AccompanyReviewRequest;
import connectripbe.connectrip_be.review.dto.AccompanyReviewResponse;
import java.util.List;

public interface AccompanyReviewService {
    AccompanyReviewResponse createReview(Long chatRoomId, Long memberId, AccompanyReviewRequest reviewRequest);

    List<AccompanyReviewResponse> getReviewsByChatRoomId(Long chatRoomId);
}
