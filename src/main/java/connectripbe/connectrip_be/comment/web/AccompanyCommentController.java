package connectripbe.connectrip_be.comment.web;

import connectripbe.connectrip_be.auth.config.LoginUser;
import connectripbe.connectrip_be.comment.dto.AccompanyCommentRequest;
import connectripbe.connectrip_be.comment.dto.AccompanyCommentResponse;
import connectripbe.connectrip_be.comment.service.AccompanyCommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/comment")
@RequiredArgsConstructor
public class AccompanyCommentController {

    private final AccompanyCommentService accompanyCommentService;

    /**
     * 특정 게시물의 댓글 목록 조회.
     * 주어진 게시물 ID에 해당하는 모든 댓글을 조회하여 반환합니다.
     *
     * @param postId 댓글을 조회할 게시물의 ID
     * @return 댓글 목록을 담은 ResponseEntity<List<AccompanyCommentResponse>>
     */
    @GetMapping
    public ResponseEntity<List<AccompanyCommentResponse>> getCommentList(@RequestParam("postId") Long postId) {
        List<AccompanyCommentResponse> comments = accompanyCommentService.getCommentsByPost(postId);
        return ResponseEntity.ok(comments);
    }

    /**
     * 댓글 생성.
     * 주어진 요청 정보와 로그인한 사용자의 이메일을 이용해 댓글을 생성하고, 생성된 댓글을 반환합니다.
     * 로그인한 사용자만 댓글을 작성할 수 있습니다.
     *
     * @param request 댓글 생성 요청 정보 (게시물 ID, 댓글 내용 포함)
     * @param email 로그인한 사용자의 이메일
     * @return 생성된 댓글 정보를 담은 ResponseEntity<AccompanyCommentResponse>
     */
    @PostMapping
    public ResponseEntity<AccompanyCommentResponse> createComment(@RequestBody @Valid AccompanyCommentRequest request, @LoginUser String email) {
        AccompanyCommentResponse response = accompanyCommentService.createComment(request, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 댓글 삭제.
     * 주어진 댓글 ID에 해당하는 댓글을 삭제합니다.
     * 로그인한 사용자만 자신의 댓글을 삭제할 수 있습니다.
     *
     * @param commentId 삭제할 댓글의 ID
     * @param email 로그인한 사용자의 이메일
     * @return 204 No Content 상태 코드를 담은 ResponseEntity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable("id") Long commentId, @LoginUser String email) {
        accompanyCommentService.deleteComment(commentId, email);
        return ResponseEntity.noContent().build(); // 204 No Content 반환
    }

    /**
     * 댓글 수정.
     * 주어진 요청 정보와 댓글 ID를 이용해 댓글을 수정하고, 수정된 댓글을 반환합니다.
     * 로그인한 사용자만 자신의 댓글을 수정할 수 있습니다.
     *
     * @param commentId 수정할 댓글의 ID
     * @param request 댓글 수정 요청 정보 (수정된 댓글 내용 포함)
     * @param email 로그인한 사용자의 이메일
     * @return 수정된 댓글 정보를 담은 ResponseEntity<AccompanyCommentResponse>
     */
    @PutMapping("/{id}")
    public ResponseEntity<AccompanyCommentResponse> updateComment(
            @PathVariable("id") Long commentId,
            @RequestBody @Valid AccompanyCommentRequest request,
            @LoginUser String email) {
        AccompanyCommentResponse response = accompanyCommentService.updateComment(request, commentId, email);
        return ResponseEntity.ok(response);
    }

}
