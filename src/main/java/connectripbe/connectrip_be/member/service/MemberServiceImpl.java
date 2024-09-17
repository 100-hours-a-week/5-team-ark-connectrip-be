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
import connectripbe.connectrip_be.member.dto.ProfileUpdateRequestDto;
import connectripbe.connectrip_be.member.dto.TokenAndHeaderInfoDto;
import connectripbe.connectrip_be.member.entity.MemberEntity;
import connectripbe.connectrip_be.member.entity.enums.AgeGroup;
import connectripbe.connectrip_be.member.entity.type.MemberLoginType;
import connectripbe.connectrip_be.member.entity.type.MemberRoleType;
import connectripbe.connectrip_be.member.exception.DuplicateMemberNicknameException;
import connectripbe.connectrip_be.member.exception.NotFoundMemberException;
import connectripbe.connectrip_be.member.repository.MemberJpaRepository;
import java.time.LocalDate;
import java.time.Period;
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

    @Override
    public GlobalResponse<CheckDuplicateEmailDto> checkDuplicateEmail(String email) {
        boolean existsByEmail = memberJpaRepository.existsByEmail(email);
        return new GlobalResponse<>(existsByEmail ? "DUPLICATED_EMAIL" : "SUCCESS",
                new CheckDuplicateEmailDto(existsByEmail));
    }

    @Override
    public GlobalResponse<CheckDuplicateNicknameDto> checkDuplicateNickname(String nickname) {
        boolean existsByNickname = memberJpaRepository.existsByNickname(nickname);
        return new GlobalResponse<>(existsByNickname ? "DUPLICATED_NICKNAME" : "SUCCESS",
                new CheckDuplicateNicknameDto(existsByNickname));
    }

    @Override
    public GlobalResponse<MemberHeaderInfoDto> getMemberHeaderInfo(Long id) {
        MemberEntity memberEntity = memberJpaRepository.findById(id).orElseThrow(NotFoundMemberException::new);
        return new GlobalResponse<>(memberEntity.getNickname() == null ? "FIRST_LOGIN" : "SUCCESS",
                MemberHeaderInfoDto.fromEntity(memberEntity));
    }

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

    // 프로필 조회 메서드 (최신 3개의 리뷰만 가져오기)
    @Override
    public ProfileDto getProfile(Long memberId) {
        MemberEntity member = memberJpaRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        // 최신 3개 리뷰 가져오기 (네이티브 쿼리 사용)
        List<AccompanyReviewResponse> recentReviews = accompanyReviewRepository.findRecentReviewsByTargetId(memberId)
                .stream()
                .map(AccompanyReviewResponse::fromEntity)
                .collect(Collectors.toList());

        // 전체 리뷰 수 가져오기
        int reviewCount = accompanyReviewRepository.countByTargetId(memberId);

        // 나이 계산 및 나이대 결정
        int age = calculateAge(member.getBirthDate().toLocalDate());
        String ageGroup = calculateAgeGroup(age);

        return ProfileDto.fromEntity(member, recentReviews, reviewCount, ageGroup);
    }

    @Override
    public List<AccompanyReviewResponse> getAllReviews(Long memberId) {
        return accompanyReviewRepository.findAllByTargetId(memberId).stream()
                .map(AccompanyReviewResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public String calculateAgeGroup(int age) {
        return AgeGroup.fromAge(age).getLabel();
    }

    private int calculateAge(LocalDate birthDate) {
        LocalDate now = LocalDate.now();
        return Period.between(birthDate, now).getYears();
    }

    // 프로필 수정 메서드 추가 (닉네임과 자기소개만 수정 가능)
    @Override
    public ProfileDto updateProfile(Long memberId, ProfileUpdateRequestDto dto) {
        MemberEntity member = memberJpaRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        // 닉네임과 자기소개 업데이트
        if (dto.getNickname() != null && !dto.getNickname().isEmpty()) {
            member.setNickname(dto.getNickname());
        }
        if (dto.getDescription() != null && !dto.getDescription().isEmpty()) {
            member.setDescription(dto.getDescription());
        }

        // 변경된 내용 저장
        memberJpaRepository.save(member);

        // 리뷰 카운트 및 최신 리뷰 불러오기
        int reviewCount = accompanyReviewRepository.countByTargetId(memberId);
        List<AccompanyReviewResponse> recentReviews = accompanyReviewRepository.findRecentReviewsByTargetId(memberId)
                .stream()
                .map(AccompanyReviewResponse::fromEntity)
                .collect(Collectors.toList());

        // 나이대 계산
        String ageGroup = calculateAgeGroup(calculateAge(member.getBirthDate().toLocalDate()));

        // ProfileDto 반환
        return ProfileDto.fromEntity(member, recentReviews, reviewCount, ageGroup);
    }
}
