package connectripbe.connectrip_be.communitypost.dto;

import java.util.List;

public record SearchCommunityPostSummaryResponse(
        long totalCommunityPosts,
        List<CommunityPostResponse> communityPosts) {
}
