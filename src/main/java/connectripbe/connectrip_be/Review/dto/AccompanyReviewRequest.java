package connectripbe.connectrip_be.Review.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccompanyReviewRequest {
    private Long reviewerId;
    private Long targetId;
    private Long accompanyPostId;
    private String content;
}
