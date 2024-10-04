package connectripbe.connectrip_be.accompany.comment.web;

import connectripbe.connectrip_be.accompany.comment.dto.AccompanyCommentRequest;
import connectripbe.connectrip_be.accompany.comment.dto.AccompanyCommentResponse;
import connectripbe.connectrip_be.accompany.comment.service.AccompanyCommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/comment")
@RequiredArgsConstructor
@Tag(name = "Accompany Comment", description = "Accompany 게시물의 댓글 관리")
public class AccompanyCommentController {

    private final AccompanyCommentService accompanyCommentService;

    /**
     * 특정 게시물의 댓글 목록 조회. 주어진 게시물 ID에 해당하는 모든 댓글을 조회하여 반환합니다.
     *
     * @param postId 댓글을 조회할 게시물의 ID
     * @return 댓글 목록을 담은 ResponseEntity<List<AccompanyCommentResponse>>
     */
    @GetMapping("/{postId}")
    public ResponseEntity<List<AccompanyCommentResponse>> getCommentList(
            @PathVariable Long postId
    ) {
        List<AccompanyCommentResponse> comments = accompanyCommentService.getCommentsByPost(postId);
        return ResponseEntity.ok(comments);
    }

    /**
     * 댓글 생성. 주어진 요청 정보와 로그인한 사용자의 이메일을 이용해 댓글을 생성하고, 생성된 댓글을 반환합니다. 로그인한 사용자만 댓글을 작성할 수 있습니다.
     *
     * @param request  댓글 생성 요청 정보 (게시물 ID, 댓글 내용 포함)
     * @param memberId 로그인한 사용자의 아이디
     * @return 생성된 댓글 정보를 담은 ResponseEntity<AccompanyCommentResponse>
     */
    @PostMapping
    public ResponseEntity<AccompanyCommentResponse> createComment(
            @AuthenticationPrincipal Long memberId,
            @RequestBody @Valid AccompanyCommentRequest request
    ) {
        AccompanyCommentResponse response = accompanyCommentService.createComment(memberId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 댓글 삭제. 주어진 댓글 ID에 해당하는 댓글을 삭제합니다. 로그인한 사용자만 자신의 댓글을 삭제할 수 있습니다.
     *
     * @param memberId  로그인한 사용자의 아이디
     * @param commentId 삭제할 댓글의 아이디
     * @return 204 No Content 상태 코드를 담은 ResponseEntity
     */
    @PostMapping("/{id}")
    public ResponseEntity<?> deleteComment(
            @AuthenticationPrincipal Long memberId,
            @PathVariable("id") Long commentId
    ) {
        accompanyCommentService.deleteComment(memberId, commentId);
        return ResponseEntity.ok().build();
    }

    /**
     * 댓글 수정. 주어진 요청 정보와 댓글 ID를 이용해 댓글을 수정하고, 수정된 댓글을 반환합니다. 로그인한 사용자만 자신의 댓글을 수정할 수 있습니다.
     *
     * @param commentId 수정할 댓글의 ID
     * @param request   댓글 수정 요청 정보 (수정된 댓글 내용 포함)
     * @param memberId  로그인한 사용자의 아이디
     * @return 수정된 댓글 정보를 담은 ResponseEntity<AccompanyCommentResponse>
     */
    @PutMapping("/{id}")
    public ResponseEntity<AccompanyCommentResponse> updateComment(
            @AuthenticationPrincipal Long memberId,
            @PathVariable("id") Long commentId,
            @RequestBody @Valid AccompanyCommentRequest request
    ) {
        AccompanyCommentResponse response = accompanyCommentService.updateComment(memberId, commentId, request);
        return ResponseEntity.ok(response);
    }
}
