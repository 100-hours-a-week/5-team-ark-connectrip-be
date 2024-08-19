package connectripbe.connectrip_be.comment.service;

import connectripbe.connectrip_be.comment.dto.AccompanyCommentRequest;
import connectripbe.connectrip_be.comment.dto.AccompanyCommentResponse;

import java.util.List;

public interface AccompanyCommentService {

    // 댓글 생성
    AccompanyCommentResponse createComment(AccompanyCommentRequest request, String email);

    // 댓글 삭제
    void deleteComment(Long commentId);

    // 특정 게시물에 달린 삭제되지 않은 댓글 목록 조회
    List<AccompanyCommentResponse> getCommentsByPost(Long postId);

    // 댓글 수정
    AccompanyCommentResponse updateComment(AccompanyCommentRequest request, Long commentId, String email);
}
