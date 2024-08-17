package connectripbe.connectrip_be.accompany_status.service;

import connectripbe.connectrip_be.accompany_status.response.AccompanyStatusResponse;

public interface AccompanyStatusService {

    AccompanyStatusResponse getAccompanyStatus(long AccompanyPostId);

    void updateAccompanyStatus(String memberEmail, long postId);
}
