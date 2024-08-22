package connectripbe.connectrip_be.pending_list.service;


import connectripbe.connectrip_be.pending_list.dto.PendingListResponse;

public interface PendingListService {

    //TODO 게시물 작성자가 해당 게시물에 신청한 사용자들의 리스트를 조회하는 api

    // 현재 로그인 한 사용자가 게시물에 신청된 상태 확인 api
     PendingListResponse getMyPendingStatus(Long accompanyPostId,String email);

    // 사용자가 해당 게시물에 신청하는 api
    PendingListResponse accompanyPending(Long accompanyPostId,String email);


}
