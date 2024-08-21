package connectripbe.connectrip_be.member.service;

import connectripbe.connectrip_be.global.dto.GlobalResponse;
import connectripbe.connectrip_be.member.dto.FirstUpdateMemberRequest;
import connectripbe.connectrip_be.member.dto.MemberHeaderInfoDto;

public interface MemberService {

    GlobalResponse<MemberHeaderInfoDto> getMemberHeaderInfo(String email);

    // fixme-noah: 추후 달라지면 response 분리
    GlobalResponse<MemberHeaderInfoDto> getFirstUpdateMemberResponse(String email, FirstUpdateMemberRequest request);
}
