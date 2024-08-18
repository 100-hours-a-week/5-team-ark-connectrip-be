package connectripbe.connectrip_be.comment.service;

import connectripbe.connectrip_be.comment.dto.AccompanyCommentRequest;
import connectripbe.connectrip_be.comment.dto.AccompanyCommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccompanyCommentService {
    // 댓글 생성
    AccompanyCommentResponse createComment(AccompanyCommentRequest request);

    // 댓글 삭제
    void deleteComment(Long commentId);

    // 특정 게시물에 달린 댓글 목록 조회
    Page<AccompanyCommentResponse> getCommentsByPost(Long postId, Pageable pageable);
}
