package connectripbe.connectrip_be.comment.service;

import connectripbe.connectrip_be.comment.dto.AccompanyCommentRequest;
import connectripbe.connectrip_be.comment.dto.AccompanyCommentResponse;

import java.util.List;

public interface AccompanyCommentService {

    /**
     * 댓글 생성
     * @param request 댓글 생성 요청 정보
     * @param email 댓글 작성자의 이메일
     * @return 생성된 댓글 정보
     */
    AccompanyCommentResponse createComment(AccompanyCommentRequest request, String email);

    /**
     * 댓글 삭제
     * @param commentId 삭제할 댓글의 ID
     * @param email 삭제하려는 사용자의 이메일
     */
    void deleteComment(Long commentId, String email);

    /**
     * 특정 게시물에 달린 삭제되지 않은 댓글 목록 조회
     * @param postId 조회할 게시물의 ID
     * @return 삭제되지 않은 댓글 목록
     */
    List<AccompanyCommentResponse> getCommentsByPost(Long postId);

    /**
     * 댓글 수정
     * @param request 댓글 수정 요청 정보
     * @param commentId 수정할 댓글의 ID
     * @param email 수정하려는 사용자의 이메일
     * @return 수정된 댓글 정보
     */
    AccompanyCommentResponse updateComment(AccompanyCommentRequest request, Long commentId, String email);
}
