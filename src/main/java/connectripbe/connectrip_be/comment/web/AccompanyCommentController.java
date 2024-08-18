package connectripbe.connectrip_be.comment.web;

import connectripbe.connectrip_be.comment.dto.AccompanyCommentRequest;
import connectripbe.connectrip_be.comment.dto.AccompanyCommentResponse;
import connectripbe.connectrip_be.comment.service.AccompanyCommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/comment")
@RequiredArgsConstructor
public class AccompanyCommentController {

    private final AccompanyCommentService accompanyCommentService;

    /**
     * 특정 게시물의 댓글 목록 조회
     */
    @GetMapping
    public ResponseEntity<Page<AccompanyCommentResponse>> getCommentList(@RequestParam("postId") Long postId,
                                                                         @PageableDefault(sort = "createdDate", direction = Direction.DESC) Pageable pageable) {
        Page<AccompanyCommentResponse> comments = accompanyCommentService.getCommentsByPost(postId, pageable);
        return ResponseEntity.ok(comments);
    }

    /**
     * 댓글 생성
     */
    @PostMapping
    public ResponseEntity<AccompanyCommentResponse> createComment(@RequestBody @Valid AccompanyCommentRequest request) {
        AccompanyCommentResponse response = accompanyCommentService.createComment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable("id") Long commentId) {
        accompanyCommentService.deleteComment(commentId);
        return ResponseEntity.noContent().build(); // 204 No Content 반환
    }
}
