package connectripbe.connectrip_be.review.dto;

import java.util.List;

public record AccompanyReviewListResponse(
        Long targetId,
        String targetNickname,
        Integer reviewCount,
        List<AccompanyReviewDto> reviews
) {
}
