package connectripbe.connectrip_be.pending_list.service;


import connectripbe.connectrip_be.pending_list.dto.PendingListResponse;
import connectripbe.connectrip_be.pending_list.dto.PendingResponse;
import java.util.List;

public interface PendingListService {

    //TODO 게시물 작성자가 해당 게시물에 신청한 사용자들의 리스트를 조회하는 api
    // - 게시물 작성자 or 채팅방 방장만 볼 수 있음, 신청 또는 수락이 된다면 리스트에 출력 X
    List<PendingListResponse> getPendingList(Long memberId, Long accompanyPostId);

    // 현재 로그인 한 사용자가 게시물에 신청된 상태 확인 api
    PendingResponse getMyPendingStatus(Long memberId, Long accompanyPostId);

    // 사용자가 해당 게시물에 신청하는 api
    PendingResponse accompanyPending(Long memberId, Long accompanyPostId);

    // TODO 신청자가 해당 신청을 취소하는 api
    // - 신청자가 해당 신청을 취소한다면 다시 신청 가능
    PendingResponse cancelPending(Long memberId, Long accompanyPostId);

    // TODO 게시물 작성자가 해당 신청을 수락하는 api
    // - 신청을 수락한다면(수락된다면) 채팅방 참여 멤버로 등록
    PendingResponse acceptPending(Long memberId, Long accompanyPostId);

    // TODO 게시물 작성자가 해당 신청을 거절하는 api
    // - 신청자가 거절 당한다면 해당 게시물에 다시 신청 못 함.
    PendingResponse rejectPending(Long memberId, Long accompanyPostId);


}
