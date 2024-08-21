package connectripbe.connectrip_be.post.service;

import connectripbe.connectrip_be.post.dto.AccompanyPostListResponse;
import connectripbe.connectrip_be.post.dto.AccompanyPostRequest;
import connectripbe.connectrip_be.post.dto.AccompanyPostResponse;

import java.util.List;


public interface AccompanyPostService {

    AccompanyPostResponse createAccompanyPost(String memberEmail, AccompanyPostRequest request);

    AccompanyPostResponse readAccompanyPost(long id);

    AccompanyPostResponse updateAccompanyPost(String memberEmail, long id, AccompanyPostRequest request);

    void deleteAccompanyPost(String memberEmail, long id);

    //TODO 게시물 전체 리스트 - 페이징 처리
    List<AccompanyPostListResponse> accompanyPostList();

    List<AccompanyPostListResponse> searchByQuery(String query);
}
