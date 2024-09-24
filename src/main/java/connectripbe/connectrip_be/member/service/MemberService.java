package connectripbe.connectrip_be.member.service;

import connectripbe.connectrip_be.global.dto.GlobalResponse;
import connectripbe.connectrip_be.member.dto.CheckDuplicateEmailDto;
import connectripbe.connectrip_be.member.dto.CheckDuplicateNicknameDto;
import connectripbe.connectrip_be.member.dto.FirstUpdateMemberRequest;
import connectripbe.connectrip_be.member.dto.MemberHeaderInfoDto;
import connectripbe.connectrip_be.member.dto.ProfileDto;
import connectripbe.connectrip_be.member.dto.ProfileUpdateRequestDto;
import connectripbe.connectrip_be.member.dto.TokenAndHeaderInfoDto;
import connectripbe.connectrip_be.review.dto.AccompanyReviewResponse2;

public interface MemberService {

    GlobalResponse<CheckDuplicateEmailDto> checkDuplicateEmail(String email);

    GlobalResponse<CheckDuplicateNicknameDto> checkDuplicateNickname(String nickname);

    GlobalResponse<MemberHeaderInfoDto> getMemberHeaderInfo(Long id);

    TokenAndHeaderInfoDto getFirstUpdateMemberResponse(String tempTokenCookie, FirstUpdateMemberRequest request);

    ProfileDto getProfile(Long memberId);  // 프로필 조회 (최신 3개 리뷰 포함)

    AccompanyReviewResponse2 getAllReviews(Long memberId);  // 모든 리뷰 조회

    String calculateAgeGroup(int age);  // 나이대 계산 메서드

    void updateProfile(Long memberId, ProfileUpdateRequestDto dto);  // 프로필 업데이트 메서드
}
