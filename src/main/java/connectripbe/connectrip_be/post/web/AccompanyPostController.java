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

    @PostMapping
    public ResponseEntity<Void> createAccompanyPost(@LoginUser String memberEmail, @RequestBody AccompanyPostRequest request) {
        accompanyPostService.createAccompanyPost(memberEmail, request);

        return ResponseEntity.ok().build();
    }

    // 게시글 조회
    @GetMapping("/{id}")
    public ResponseEntity<AccompanyPostResponse> readAccompanyPost(@PathVariable Long id) {
        return ResponseEntity.ok(accompanyPostService.readAccompanyPost(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateAccompanyPost(
            @LoginUser String memberEmail,
            @PathVariable Long id,
            @RequestBody AccompanyPostRequest request) {
        accompanyPostService.updateAccompanyPost(memberEmail, id, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccompanyPost(
            @LoginUser String memberEmail,
            @PathVariable Long id) {
        accompanyPostService.deleteAccompanyPost(memberEmail, id);

        return ResponseEntity.ok().build();
    }

    // fixme-naoh: 나중에 수정
    // 게시글 목록 조회 (페이징 처리)
    @GetMapping
    public ResponseEntity<Page<AccompanyPostResponse>> listPosts(Pageable pageable) {
        return ResponseEntity.ok(accompanyPostService.accompanyPostList(pageable));
    }
}
