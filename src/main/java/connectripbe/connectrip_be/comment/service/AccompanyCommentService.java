package connectripbe.connectrip_be.comment.service;

import connectripbe.connectrip_be.comment.dto.AccompanyCommentRequest;
import connectripbe.connectrip_be.comment.dto.AccompanyCommentResponse;

public interface AccompanyCommentService {

    // 댓글 생성 메서드
    AccompanyCommentResponse createComment(AccompanyCommentRequest request);

    // 댓글 삭제 메서드
    void deleteComment(Long commentId);
}
