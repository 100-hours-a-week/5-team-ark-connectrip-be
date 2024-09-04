package connectripbe.connectrip_be.communitycomment.service;

import connectripbe.connectrip_be.communitycomment.dto.CommunityCommentRequest;
import connectripbe.connectrip_be.communitycomment.dto.CommunityCommentResponse;
import java.util.List;

public interface CommunityCommentService {

    CommunityCommentResponse createComment(Long memberId, CommunityCommentRequest request);

    void deleteComment(Long memberId, Long commentId);

    List<CommunityCommentResponse> getCommentsByPost(Long postId);

    CommunityCommentResponse updateComment(Long memberId, Long commentId, CommunityCommentRequest request);
}
