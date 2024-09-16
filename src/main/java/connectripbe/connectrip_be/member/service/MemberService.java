package connectripbe.connectrip_be.member.service;

import connectripbe.connectrip_be.Review.dto.AccompanyReviewResponse;
import connectripbe.connectrip_be.global.dto.GlobalResponse;
import connectripbe.connectrip_be.member.dto.CheckDuplicateEmailDto;
import connectripbe.connectrip_be.member.dto.CheckDuplicateNicknameDto;
import connectripbe.connectrip_be.member.dto.FirstUpdateMemberRequest;
import connectripbe.connectrip_be.member.dto.MemberHeaderInfoDto;
import connectripbe.connectrip_be.member.dto.ProfileDto;
import connectripbe.connectrip_be.member.dto.ProfileUpdateRequestDto;
import connectripbe.connectrip_be.member.dto.TokenAndHeaderInfoDto;
import java.util.List;

public interface MemberService {

    GlobalResponse<CheckDuplicateEmailDto> checkDuplicateEmail(String email);

    GlobalResponse<CheckDuplicateNicknameDto> checkDuplicateNickname(String nickname);

    GlobalResponse<MemberHeaderInfoDto> getMemberHeaderInfo(Long id);

    TokenAndHeaderInfoDto getFirstUpdateMemberResponse(
            String tempTokenCookie,
            FirstUpdateMemberRequest request);

    ProfileDto getProfile(Long memberId);  // 프로필 조회 (최신 3개 리뷰)

    List<AccompanyReviewResponse> getAllReviews(Long memberId);  // 모든 리뷰 조회

    // 나이대 계산 메서드
    String calculateAgeGroup(int age);

    // 반환 타입을 ProfileDto로 변경
    ProfileDto updateProfile(Long memberId, ProfileUpdateRequestDto dto);
}
