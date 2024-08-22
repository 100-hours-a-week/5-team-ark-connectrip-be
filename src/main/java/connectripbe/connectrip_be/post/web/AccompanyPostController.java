package connectripbe.connectrip_be.post.web;

import connectripbe.connectrip_be.auth.config.LoginUser;
import connectripbe.connectrip_be.post.dto.AccompanyPostListResponse;
import connectripbe.connectrip_be.post.dto.CreateAccompanyPostRequest;
import connectripbe.connectrip_be.post.dto.AccompanyPostResponse;
import connectripbe.connectrip_be.post.dto.UpdateAccompanyPostRequest;
import connectripbe.connectrip_be.post.service.AccompanyPostService;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accompany/posts")
@RequiredArgsConstructor
public class AccompanyPostController {

    private final AccompanyPostService accompanyPostService;

    @PostMapping
    public ResponseEntity<AccompanyPostResponse> createAccompanyPost(@LoginUser String memberEmail,
                                                                     @RequestBody CreateAccompanyPostRequest request) {
        AccompanyPostResponse response = accompanyPostService.createAccompanyPost(memberEmail, request);
        return ResponseEntity.ok(response);  // 데이터가 포함된 응답 반환
    }

    // 게시글 조회
    @GetMapping("/{id}")
    public ResponseEntity<AccompanyPostResponse> readAccompanyPost(@PathVariable Long id) {
        AccompanyPostResponse response = accompanyPostService.readAccompanyPost(id);
        return ResponseEntity.ok(response);  // 데이터 반환
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AccompanyPostResponse> updateAccompanyPost(
            @LoginUser String memberEmail,
            @PathVariable Long id,
            @RequestBody UpdateAccompanyPostRequest request) {
        AccompanyPostResponse response = accompanyPostService.updateAccompanyPost(memberEmail, id, request);
        return ResponseEntity.ok(response);  // 수정된 데이터 반환
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccompanyPost(
            @LoginUser String memberEmail,
            @PathVariable Long id) {
        accompanyPostService.deleteAccompanyPost(memberEmail, id);
        return ResponseEntity.ok().build();  // 삭제는 빈 응답
    }

    @GetMapping
    public ResponseEntity<List<AccompanyPostListResponse>> listPosts() {
        List<AccompanyPostListResponse> response = accompanyPostService.accompanyPostList();
        return ResponseEntity.ok(response);  // 전체 리스트 반환
    }

    @GetMapping("/search")
    public ResponseEntity<List<AccompanyPostListResponse>> searchByQuery(@RequestParam String query) {
        return ResponseEntity.ok(accompanyPostService.searchByQuery(query));
    }
}
