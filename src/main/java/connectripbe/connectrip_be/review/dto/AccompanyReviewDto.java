package connectripbe.connectrip_be.review.dto;

public record AccompanyReviewDto(
        Long reviewId,
        Long reviewerId,
        String reviewerNickname,
        String reviewerProfile,
        String content,
        String createdAt
) {
}
