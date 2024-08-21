package connectripbe.connectrip_be.Review.dto;

import connectripbe.connectrip_be.Review.entity.AccompanyReviewEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AccompanyReviewResponse {
    private Long reviewId;
    private Long reviewerId;
    private Long targetId;
    private Long chatRoomId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static AccompanyReviewResponse fromEntity(AccompanyReviewEntity review) {
        return AccompanyReviewResponse.builder()
                .reviewId(review.getId())
                .reviewerId(review.getReviewer().getId())
                .targetId(review.getTarget().getId())
                .chatRoomId(review.getChatRoom().getId())
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .deletedAt(review.getDeletedAt())
                .build();
    }
}
