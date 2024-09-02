package connectripbe.connectrip_be.communitypost.controller;

import connectripbe.connectrip_be.communitypost.dto.CommunityPostResponse;
import connectripbe.connectrip_be.communitypost.dto.CreateCommunityPostRequest;
import connectripbe.connectrip_be.communitypost.service.CommunityPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/community/posts")
@RequiredArgsConstructor
public class CommunityPostController {

    private final CommunityPostService communityPostService;

    @PostMapping
    public ResponseEntity<CommunityPostResponse> createPost(
            @RequestBody CreateCommunityPostRequest request,
            @RequestParam Long memberId) {
        CommunityPostResponse response = communityPostService.createPost(request, memberId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
