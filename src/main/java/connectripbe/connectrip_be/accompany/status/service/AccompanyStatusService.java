package connectripbe.connectrip_be.accompany.status.service;

import connectripbe.connectrip_be.accompany.status.response.AccompanyStatusResponse;

public interface AccompanyStatusService {

    AccompanyStatusResponse getAccompanyStatus(long AccompanyPostId);

    void updateAccompanyStatus(Long memberId, long postId);
}
