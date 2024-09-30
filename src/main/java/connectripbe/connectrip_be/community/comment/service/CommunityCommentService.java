package connectripbe.connectrip_be.community.comment.service;

import connectripbe.connectrip_be.community.comment.dto.CommunityCommentRequest;
import connectripbe.connectrip_be.community.comment.dto.CommunityCommentResponse;
import java.util.List;

public interface CommunityCommentService {

    CommunityCommentResponse createComment(Long memberId, CommunityCommentRequest request);

    void deleteComment(Long memberId, Long commentId);

    List<CommunityCommentResponse> getCommentsByPost(Long postId);

    CommunityCommentResponse updateComment(Long memberId, Long commentId, CommunityCommentRequest request);
}
