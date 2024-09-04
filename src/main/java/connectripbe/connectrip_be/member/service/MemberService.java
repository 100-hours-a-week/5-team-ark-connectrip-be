package connectripbe.connectrip_be.member.service;

import connectripbe.connectrip_be.global.dto.GlobalResponse;
import connectripbe.connectrip_be.member.dto.CheckDuplicateEmailDto;
import connectripbe.connectrip_be.member.dto.CheckDuplicateNicknameDto;
import connectripbe.connectrip_be.member.dto.FirstUpdateMemberRequest;
import connectripbe.connectrip_be.member.dto.MemberHeaderInfoDto;
import connectripbe.connectrip_be.member.dto.TokenAndHeaderInfoDto;

public interface MemberService {

    GlobalResponse<CheckDuplicateEmailDto> checkDuplicateEmail(String email);

    GlobalResponse<CheckDuplicateNicknameDto> checkDuplicateNickname(String nickname);

    GlobalResponse<MemberHeaderInfoDto> getMemberHeaderInfo(Long id);

    // fixme-noah: 추후 달라지면 response 분리
    TokenAndHeaderInfoDto getFirstUpdateMemberResponse(
            String tempTokenCookie,
            FirstUpdateMemberRequest request);
}
