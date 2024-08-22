package connectripbe.connectrip_be.post.service;

import connectripbe.connectrip_be.post.dto.AccompanyPostListResponse;
import connectripbe.connectrip_be.post.dto.CreateAccompanyPostRequest;
import connectripbe.connectrip_be.post.dto.AccompanyPostResponse;
import connectripbe.connectrip_be.post.dto.UpdateAccompanyPostRequest;

import java.util.List;


public interface AccompanyPostService {

    void createAccompanyPost(String memberEmail, CreateAccompanyPostRequest request);

    AccompanyPostResponse readAccompanyPost(long id);

    AccompanyPostResponse updateAccompanyPost(String memberEmail, long id, UpdateAccompanyPostRequest request);

    void deleteAccompanyPost(String memberEmail, long id);

    //TODO 게시물 전체 리스트 - 페이징 처리
    List<AccompanyPostListResponse> accompanyPostList();

    List<AccompanyPostListResponse> searchByQuery(String query);

    boolean checkDuplicatedCustomUrl(String customUrl);
}
