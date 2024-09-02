package connectripbe.connectrip_be.communitypost.service;

import connectripbe.connectrip_be.communitypost.dto.CommunityPostResponse;
import connectripbe.connectrip_be.communitypost.dto.CreateCommunityPostRequest;

public interface CommunityPostService {

    CommunityPostResponse createPost(CreateCommunityPostRequest request, Long memberId);
}
