package connectripbe.connectrip_be.review.dto.response;

public record AccompanyReviewSummaryResponse(
        String revieweeNickname,
        String content,
        String createdAt
) {
}
