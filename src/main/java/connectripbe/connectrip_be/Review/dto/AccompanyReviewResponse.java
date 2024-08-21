package connectripbe.connectrip_be.Review.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AccompanyReviewResponse {
    private Long reviewId;
    private Long reviewerId;
    private Long targetId;
    private Long accompanyPostId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
