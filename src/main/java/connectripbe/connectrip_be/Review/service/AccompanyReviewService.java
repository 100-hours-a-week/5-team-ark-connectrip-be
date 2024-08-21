package connectripbe.connectrip_be.Review.service;

import connectripbe.connectrip_be.Review.dto.AccompanyReviewRequest;
import connectripbe.connectrip_be.Review.dto.AccompanyReviewResponse;

import java.util.List;

public interface AccompanyReviewService {
    AccompanyReviewResponse createReview(AccompanyReviewRequest reviewRequest);
    List<AccompanyReviewResponse> getReviewsByChatRoomId(Long chatRoomId);
}
