package connectripbe.connectrip_be.post.service;

import connectripbe.connectrip_be.post.dto.AccompanyPostResponse;
import connectripbe.connectrip_be.post.dto.CreateAccompanyPostRequest;
import connectripbe.connectrip_be.post.dto.SearchAccompanyPostSummaryResponse;
import connectripbe.connectrip_be.post.dto.UpdateAccompanyPostRequest;


public interface AccompanyPostService {

    void createAccompanyPost(Long memberId, CreateAccompanyPostRequest request);

    AccompanyPostResponse readAccompanyPost(long id);

    AccompanyPostResponse updateAccompanyPost(Long memberId, long id, UpdateAccompanyPostRequest request);

    void deleteAccompanyPost(Long memberId, long id);

    SearchAccompanyPostSummaryResponse accompanyPostList(int page);

    SearchAccompanyPostSummaryResponse searchByQuery(int page, String query);

    boolean checkDuplicatedCustomUrl(String customUrl);
}
