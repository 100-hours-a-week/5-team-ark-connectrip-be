package connectripbe.connectrip_be.accompany.post.dto;

import java.util.List;

public record SearchAccompanyPostSummaryResponse(
        long totalAccompanyPosts,
        List<AccompanyPostListResponse> accompanyPosts) {
}
