package connectripbe.connectrip_be.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccompanyReviewRequest {
    private Long targetId;
    private String content;
}
