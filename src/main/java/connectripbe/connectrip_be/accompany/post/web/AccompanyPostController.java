package connectripbe.connectrip_be.accompany.post.web;

import connectripbe.connectrip_be.accompany.post.dto.AccompanyPostResponse;
import connectripbe.connectrip_be.accompany.post.dto.CheckDuplicatedCustomUrlDto;
import connectripbe.connectrip_be.accompany.post.dto.CreateAccompanyPostRequest;
import connectripbe.connectrip_be.accompany.post.dto.SearchAccompanyPostSummaryResponse;
import connectripbe.connectrip_be.accompany.post.dto.UpdateAccompanyPostRequest;
import connectripbe.connectrip_be.accompany.post.exception.DuplicatedCustomUrlException;
import connectripbe.connectrip_be.accompany.post.service.AccompanyPostService;
import connectripbe.connectrip_be.global.dto.GlobalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/accompany/posts")
@RequiredArgsConstructor
public class AccompanyPostController {

    private final AccompanyPostService accompanyPostService;

    @PostMapping
    public ResponseEntity<GlobalResponse<?>> createAccompanyPost(@AuthenticationPrincipal Long memberId,
                                                                 @RequestBody CreateAccompanyPostRequest request) {
        accompanyPostService.createAccompanyPost(memberId, request);
        return ResponseEntity.ok(new GlobalResponse<>("SUCCESS", null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccompanyPostResponse> readAccompanyPost(@PathVariable Long id) {
        AccompanyPostResponse response = accompanyPostService.readAccompanyPost(id);
        return ResponseEntity.ok(response);  // 데이터 반환
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AccompanyPostResponse> updateAccompanyPost(@AuthenticationPrincipal Long memberId,
                                                                     @PathVariable Long id,
                                                                     @RequestBody UpdateAccompanyPostRequest request) {
        AccompanyPostResponse response = accompanyPostService.updateAccompanyPost(memberId, id, request);
        return ResponseEntity.ok(response);  // 수정된 데이터 반환
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> deleteAccompanyPost(@AuthenticationPrincipal Long memberId, @PathVariable Long id) {
        accompanyPostService.deleteAccompanyPost(memberId, id);
        return ResponseEntity.ok().build();  // 삭제는 빈 응답
    }

    @GetMapping
    public ResponseEntity<SearchAccompanyPostSummaryResponse> listPosts(
            @RequestParam(defaultValue = "1") Integer page
    ) {
        SearchAccompanyPostSummaryResponse response = accompanyPostService.accompanyPostList(page);
        return ResponseEntity.ok(response);  // 전체 리스트 반환
    }

    @GetMapping("/search")
    public ResponseEntity<SearchAccompanyPostSummaryResponse> searchByQuery(
            @RequestParam String query,
            @RequestParam(defaultValue = "1") Integer page
    ) {
        SearchAccompanyPostSummaryResponse response = accompanyPostService.searchByQuery(query, page);

        return ResponseEntity.ok(response);
    }

    // fixme-noah: 추후 다르 중복 확인 메서드 모두 이름 수정, 혼동 있음
    @GetMapping("/check-custom-url")
    public ResponseEntity<GlobalResponse<CheckDuplicatedCustomUrlDto>> checkDuplicatedCustomUrl(
            @RequestParam String customUrl) {
        boolean result = accompanyPostService.checkDuplicatedCustomUrl(customUrl);

        return ResponseEntity.ok(new GlobalResponse<>(result ? "DUPLICATED_CUSTOM_URL" : "SUCCESS",
                new CheckDuplicatedCustomUrlDto(result)));
    }

    @ExceptionHandler(DuplicatedCustomUrlException.class)
    public ResponseEntity<GlobalResponse<CheckDuplicatedCustomUrlDto>> handleException(Exception e) {
        return ResponseEntity.status(409).body(new GlobalResponse<>("DUPLICATED_CUSTOM_URL", null));
    }
}
