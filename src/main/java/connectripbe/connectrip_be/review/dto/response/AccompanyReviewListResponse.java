package connectripbe.connectrip_be.review.dto.response;

import connectripbe.connectrip_be.review.dto.AccompanyReviewDto;
import java.util.List;

public record AccompanyReviewListResponse(
        Long targetId,
        String targetNickname,
        Integer reviewCount,
        List<AccompanyReviewDto> reviews
) {
}
