package connectripbe.connectrip_be.communitypost.service;

import connectripbe.connectrip_be.communitypost.dto.CommunityPostResponse;
import connectripbe.connectrip_be.communitypost.dto.CreateCommunityPostRequest;
import connectripbe.connectrip_be.communitypost.dto.SearchCommunityPostSummaryResponse;
import connectripbe.connectrip_be.communitypost.dto.UpdateCommunityPostRequest;
import java.util.List;

public interface CommunityPostService {

    CommunityPostResponse createPost(CreateCommunityPostRequest request, Long memberId);

    CommunityPostResponse updatePost(Long memberId, Long postId, UpdateCommunityPostRequest request);

    void deletePost(Long memberId, Long postId);

    CommunityPostResponse readPost(Long postId);

    SearchCommunityPostSummaryResponse getAllPosts(int page);

    List<CommunityPostResponse> getAllPostsByQuery(String query);
}
