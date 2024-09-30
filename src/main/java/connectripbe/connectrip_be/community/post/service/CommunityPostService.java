package connectripbe.connectrip_be.community.post.service;

import connectripbe.connectrip_be.community.post.dto.CommunityPostResponse;
import connectripbe.connectrip_be.community.post.dto.CreateCommunityPostRequest;
import connectripbe.connectrip_be.community.post.dto.SearchCommunityPostSummaryResponse;
import connectripbe.connectrip_be.community.post.dto.UpdateCommunityPostRequest;

public interface CommunityPostService {

    CommunityPostResponse createPost(CreateCommunityPostRequest request, Long memberId);

    CommunityPostResponse updatePost(Long memberId, Long postId, UpdateCommunityPostRequest request);

    void deletePost(Long memberId, Long postId);

    CommunityPostResponse readPost(Long postId);

    SearchCommunityPostSummaryResponse getAllPosts(int page);

    SearchCommunityPostSummaryResponse getAllPostsByQuery(String query, int page);
}
