package connectripbe.connectrip_be.post.service;

import connectripbe.connectrip_be.post.dto.AccompanyPostRequest;
import connectripbe.connectrip_be.post.dto.AccompanyPostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccompanyPostService {

    void createAccompanyPost(String memberEmail, AccompanyPostRequest request);

    AccompanyPostResponse readAccompanyPost(long id);

    void updateAccompanyPost(String memberEmail, long id, AccompanyPostRequest request);

    void deleteAccompanyPost(String memberEmail, long id);

    // 게시물 전체 리스트 - 페이징 처리
    Page<AccompanyPostResponse> accompanyPostList(Pageable pageable);
}
