package connectripbe.connectrip_be.Review.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccompanyReviewRequest {
    private Long reviewerId;
    private Long targetId;
    private Long accompanyPostId;
    private String content;

    @Builder
    public AccompanyReviewRequest(Long reviewerId, Long targetId, Long accompanyPostId, String content) {
        this.reviewerId = reviewerId;
        this.targetId = targetId;
        this.accompanyPostId = accompanyPostId;
        this.content = content;
    }
}
