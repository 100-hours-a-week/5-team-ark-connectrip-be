package connectripbe.connectrip_be.post.dto;

import java.util.List;

public record SearchAccompanyPostSummaryResponse(
        long totalAccompanyPosts,
        List<AccompanyPostListResponse> accompanyPosts) {
}
