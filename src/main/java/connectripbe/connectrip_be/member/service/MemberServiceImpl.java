package connectripbe.connectrip_be.member.service;

import connectripbe.connectrip_be.Review.dto.AccompanyReviewResponse;
import connectripbe.connectrip_be.Review.repository.AccompanyReviewRepository;
import connectripbe.connectrip_be.auth.jwt.JwtProvider;
import connectripbe.connectrip_be.auth.jwt.dto.MemberEmailAndProfileImagePathDto;
import connectripbe.connectrip_be.auth.jwt.dto.TokenDto;
import connectripbe.connectrip_be.global.dto.GlobalResponse;
import connectripbe.connectrip_be.global.exception.GlobalException;
import connectripbe.connectrip_be.global.exception.type.ErrorCode;
import connectripbe.connectrip_be.member.dto.CheckDuplicateEmailDto;
import connectripbe.connectrip_be.member.dto.CheckDuplicateNicknameDto;
import connectripbe.connectrip_be.member.dto.FirstUpdateMemberRequest;
import connectripbe.connectrip_be.member.dto.MemberHeaderInfoDto;
import connectripbe.connectrip_be.member.dto.ProfileDto;
import connectripbe.connectrip_be.member.dto.TokenAndHeaderInfoDto;
import connectripbe.connectrip_be.member.entity.MemberEntity;
import connectripbe.connectrip_be.member.entity.type.MemberLoginType;
import connectripbe.connectrip_be.member.entity.type.MemberRoleType;
import connectripbe.connectrip_be.member.exception.DuplicateMemberNicknameException;
import connectripbe.connectrip_be.member.exception.NotFoundMemberException;
import connectripbe.connectrip_be.member.repository.MemberJpaRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberJpaRepository memberJpaRepository;
    private final AccompanyReviewRepository accompanyReviewRepository;
    private final JwtProvider jwtProvider;

    @Transactional(readOnly = true)
    @Override
    public GlobalResponse<CheckDuplicateEmailDto> checkDuplicateEmail(String email) {
        boolean existsByEmail = memberJpaRepository.existsByEmail(email);

        return new GlobalResponse<>(existsByEmail ? "DUPLICATED_EMAIL" : "SUCCESS",
                new CheckDuplicateEmailDto(existsByEmail));
    }

    @Transactional(readOnly = true)
    @Override
    public GlobalResponse<CheckDuplicateNicknameDto> checkDuplicateNickname(String nickname) {
        boolean existsByNickname = memberJpaRepository.existsByNickname(nickname);

        return new GlobalResponse<>(existsByNickname ? "DUPLICATED_NICKNAME" : "SUCCESS",
                new CheckDuplicateNicknameDto(existsByNickname));
    }

    @Transactional(readOnly = true)
    @Override
    public GlobalResponse<MemberHeaderInfoDto> getMemberHeaderInfo(Long id) {
        MemberEntity memberEntity = memberJpaRepository.findById(id)
                .orElseThrow(NotFoundMemberException::new);  // NotFoundMemberException 사용

        return new GlobalResponse<>(memberEntity.getNickname() == null ? "FIRST_LOGIN" : "SUCCESS",
                MemberHeaderInfoDto.fromEntity(memberEntity));
    }

    @Transactional
    @Override
    public TokenAndHeaderInfoDto getFirstUpdateMemberResponse(String tempTokenCookie,
                                                              FirstUpdateMemberRequest request) {
        if (!jwtProvider.validateToken(tempTokenCookie)) {
            throw new GlobalException(ErrorCode.INVALID_TOKEN);
        }

        if (memberJpaRepository.existsByNickname(request.nickname())) {
            throw new DuplicateMemberNicknameException();
        }

        MemberEmailAndProfileImagePathDto memberProfileImagePathDto = jwtProvider.getMemberProfileImagePathDtoFromToken(
                tempTokenCookie);

        MemberEntity newMemberEntity = MemberEntity.builder()
                .email(memberProfileImagePathDto.memberEmail())
                .profileImagePath(memberProfileImagePathDto.memberProfileImagePath())
                .gender(request.gender())
                .nickname(request.nickname())
                .birthDate(request.birthDate())
                .loginType(MemberLoginType.KAKAO)
                .roleType(MemberRoleType.USER)
                .build();

        MemberEntity savedMemberEntity = memberJpaRepository.save(newMemberEntity);

        String refreshToken = jwtProvider.generateRefreshToken(savedMemberEntity.getId());
        String accessToken = jwtProvider.generateAccessToken(savedMemberEntity.getId());

        TokenDto tokenDto = TokenDto.builder()
                .refreshToken(refreshToken)
                .refreshTokenExpirationTime(jwtProvider.getRefreshTokenExpirationTime() / 1000)
                .accessToken(accessToken)
                .accessTokenExpirationTime(jwtProvider.getAccessTokenExpirationTime() / 1000)
                .build();

        MemberHeaderInfoDto memberHeaderInfoDto = MemberHeaderInfoDto.builder()
                .memberId(savedMemberEntity.getId())
                .profileImagePath(savedMemberEntity.getProfileImagePath())
                .nickname(savedMemberEntity.getNickname())
                .build();

        return new TokenAndHeaderInfoDto(tokenDto, memberHeaderInfoDto);
    }

    // 프로필 조회 메서드
    @Override
    @Transactional(readOnly = true)
    public ProfileDto getProfile(Long memberId) {
        MemberEntity member = memberJpaRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        // 최신 3개 리뷰 가져오기
        List<AccompanyReviewResponse> recentReviews = accompanyReviewRepository.findRecentReviewsByTargetId(memberId)
                .stream()
                .limit(3)
                .map(AccompanyReviewResponse::fromEntity)
                .collect(Collectors.toList());

        // 전체 리뷰 수 가져오기
        int reviewCount = accompanyReviewRepository.findAllByTargetId(memberId).size();

        return ProfileDto.fromEntity(member, recentReviews, reviewCount);
    }

    // 전체 리뷰 조회 메서드
    @Transactional(readOnly = true)
    @Override
    public List<AccompanyReviewResponse> getAllReviews(Long memberId) {
        // 모든 리뷰 가져오기
        return accompanyReviewRepository.findAllByTargetId(memberId).stream()
                .map(AccompanyReviewResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
