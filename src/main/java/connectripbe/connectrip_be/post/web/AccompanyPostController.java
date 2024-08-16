package connectripbe.connectrip_be.post.web;

import connectripbe.connectrip_be.auth.config.LoginUser;
import connectripbe.connectrip_be.post.dto.AccompanyPostRequest;
import connectripbe.connectrip_be.post.dto.AccompanyPostResponse;
import connectripbe.connectrip_be.post.service.AccompanyPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class AccompanyPostController {

    private final AccompanyPostService accompanyPostService;

    // 게시글 생성
    @PostMapping
    public ResponseEntity<AccompanyPostResponse> createPost(@RequestBody AccompanyPostRequest request, @LoginUser String email) {
        AccompanyPostResponse response = accompanyPostService.createPost(request, email);
        return ResponseEntity.ok(response);
    }

    // 게시글 조회
    @GetMapping("/{postId}")
    public ResponseEntity<AccompanyPostResponse> readPost(@PathVariable Long postId) {
        AccompanyPostResponse response = accompanyPostService.readPost(postId);
        return ResponseEntity.ok(response);
    }

    // 게시글 수정
    @PutMapping("/{postId}")
    public ResponseEntity<AccompanyPostResponse> updatePost(
            @PathVariable Long postId,
            @RequestBody AccompanyPostRequest request,
            @RequestParam String email) {
        AccompanyPostResponse response = accompanyPostService.updatePost(postId, request, email);
        return ResponseEntity.ok(response);
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId, @RequestParam String email) {
        accompanyPostService.deletePost(postId, email);
        return ResponseEntity.noContent().build();
    }

    // 게시글 목록 조회 (페이징 처리)
    @GetMapping
    public ResponseEntity<Page<AccompanyPostResponse>> listPosts(Pageable pageable) {
        Page<AccompanyPostResponse> responses = accompanyPostService.postList(pageable);
        return ResponseEntity.ok(responses);
    }
}
