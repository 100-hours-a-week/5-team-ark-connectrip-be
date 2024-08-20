package connectripbe.connectrip_be.member.service;

import connectripbe.connectrip_be.member.dto.FirstUpdateMemberRequest;
import connectripbe.connectrip_be.member.dto.MemberHeaderInfoResponse;

public interface MemberService {

    MemberHeaderInfoResponse getMemberHeaderInfo(String email);

    // fixme-noah: 추후 달라지면 response 분리
    MemberHeaderInfoResponse getFirstUpdateMemberResponse(String email, FirstUpdateMemberRequest request);
}
