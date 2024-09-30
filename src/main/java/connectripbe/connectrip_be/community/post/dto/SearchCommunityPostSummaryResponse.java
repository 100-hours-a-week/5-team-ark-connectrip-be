package connectripbe.connectrip_be.community.post.dto;

import java.util.List;

public record SearchCommunityPostSummaryResponse(
        long totalCommunityPosts,
        List<CommunityPostResponse> communityPosts) {
}
