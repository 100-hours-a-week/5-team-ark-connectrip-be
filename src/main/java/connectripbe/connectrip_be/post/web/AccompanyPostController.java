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
    public ResponseEntity<Void> createPost(@RequestBody AccompanyPostRequest request, @LoginUser String email) {
        accompanyPostService.createPost(request, email);

        return ResponseEntity.ok().build();
    }

    // 게시글 조회
    @GetMapping("/{postId}")
    public ResponseEntity<AccompanyPostResponse> readPost(@PathVariable Long postId) {
        AccompanyPostResponse response = accompanyPostService.readPost(postId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<Void> updatePost(
            @LoginUser String email,
            @PathVariable Long postId,
            @RequestBody AccompanyPostRequest request) {
        accompanyPostService.updatePost(postId, request, email);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @LoginUser String email,
            @PathVariable Long postId) {
        accompanyPostService.deletePost(postId, email);
        return ResponseEntity.ok().build();
    }

    // 게시글 목록 조회 (페이징 처리)
    @GetMapping
    public ResponseEntity<Page<AccompanyPostResponse>> listPosts(Pageable pageable) {
        Page<AccompanyPostResponse> responses = accompanyPostService.postList(pageable);
        return ResponseEntity.ok(responses);
    }
}
