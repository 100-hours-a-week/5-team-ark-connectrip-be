package connectripbe.connectrip_be.post.service;

import connectripbe.connectrip_be.post.dto.AccompanyPostRequest;
import connectripbe.connectrip_be.post.dto.AccompanyPostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccompanyPostService {

    // 게시글 생성
    AccompanyPostResponse createPost(AccompanyPostRequest request, String email);

    // 게시글 조회
    AccompanyPostResponse readPost(Long postId);

    // 게시글 수정
    AccompanyPostResponse updatePost(Long id, AccompanyPostRequest request, String email);

    // 게시글 삭제
    void deletePost(Long id, String email);

    // 게시물 전체 리스트 - 페이징 처리
    Page<AccompanyPostResponse> postList(Pageable pageable);

}
