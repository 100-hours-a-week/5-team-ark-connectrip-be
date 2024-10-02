package connectripbe.connectrip_be.community.post.web;

import connectripbe.connectrip_be.community.post.dto.CommunityPostResponse;
import connectripbe.connectrip_be.community.post.dto.CreateCommunityPostRequest;
import connectripbe.connectrip_be.community.post.dto.SearchCommunityPostSummaryResponse;
import connectripbe.connectrip_be.community.post.dto.UpdateCommunityPostRequest;
import connectripbe.connectrip_be.community.post.service.CommunityPostService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/community/posts")
@RequiredArgsConstructor
@Tag(name = "Community Post", description = "커뮤니티 게시물 관리")
public class CommunityPostController {

    private final CommunityPostService communityPostService;

    /**
     * 새로운 게시글을 생성하는 엔드포인트. 요청한 사용자의 ID와 요청된 데이터를 기반으로 게시글을 생성합니다.
     *
     * @param memberId 인증된 사용자의 ID (@AuthenticationPrincipal 통해 주입)
     * @param request  게시글 생성 요청 정보 (제목, 내용 포함)
     * @return 생성된 게시글의 정보를 담은 CommunityPostResponse 객체
     */
    @PostMapping
    public ResponseEntity<CommunityPostResponse> createPost(
            @AuthenticationPrincipal Long memberId,
            @RequestBody CreateCommunityPostRequest request) {
        CommunityPostResponse response = communityPostService.createPost(request, memberId);
        return ResponseEntity.ok(response);
    }

    /**
     * 기존 게시글을 수정하는 엔드포인트. 주어진 게시글 ID를 통해 게시글을 조회하고, 수정 요청된 데이터를 기반으로 업데이트합니다.
     *
     * @param memberId 인증된 사용자의 ID (@AuthenticationPrincipal 통해 주입)
     * @param postId   수정할 게시글의 ID
     * @param request  게시글 수정 요청 정보 (수정된 제목, 내용 포함)
     * @return 수정된 게시글의 정보를 담은 CommunityPostResponse 객체
     */
    @PutMapping("/{postId}")
    public ResponseEntity<CommunityPostResponse> updatePost(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long postId,
            @RequestBody UpdateCommunityPostRequest request) {
        CommunityPostResponse response = communityPostService.updatePost(memberId, postId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 기존 게시글을 삭제하는 엔드포인트. 주어진 게시글 ID를 통해 게시글을 조회하고, 삭제 권한을 확인한 후 삭제를 수행합니다.
     *
     * @param memberId 인증된 사용자의 ID (@AuthenticationPrincipal 통해 주입)
     * @param postId   삭제할 게시글의 ID
     * @return 삭제 결과로 빈 응답을 반환
     */
    @PostMapping("/{postId}/delete")
    public ResponseEntity<Void> deletePost(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long postId) {
        communityPostService.deletePost(memberId, postId);
        return ResponseEntity.ok().build();
    }

    /**
     * 특정 게시글을 조회하는 엔드포인트. 주어진 게시글 ID에 해당하는 게시글을 반환합니다.
     *
     * @param postId 조회할 게시글의 ID
     * @return 조회된 게시글의 정보를 담은 CommunityPostResponse 객체
     */
    @GetMapping("/{postId}")
    public ResponseEntity<CommunityPostResponse> readPost(@PathVariable Long postId) {
        CommunityPostResponse response = communityPostService.readPost(postId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<SearchCommunityPostSummaryResponse> getAllPosts(
            @RequestParam(defaultValue = "1") Integer page
    ) {
        SearchCommunityPostSummaryResponse response = communityPostService.getAllPosts(page);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<SearchCommunityPostSummaryResponse> searchByQuery(
            @RequestParam String query,
            @RequestParam(defaultValue = "1") Integer page
    ) {
        SearchCommunityPostSummaryResponse response = communityPostService.getAllPostsByQuery(query, page);

        return ResponseEntity.ok(response);
    }
}
