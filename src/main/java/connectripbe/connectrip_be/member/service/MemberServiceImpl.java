package connectripbe.connectrip_be.member.service;

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
import connectripbe.connectrip_be.review.dto.AccompanyReviewDto;
import connectripbe.connectrip_be.review.dto.AccompanyReviewResponse;
import connectripbe.connectrip_be.review.dto.AccompanyReviewResponse2;
import connectripbe.connectrip_be.review.entity.AccompanyReviewEntity;
import connectripbe.connectrip_be.review.repository.AccompanyReviewRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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

    /**
     * 이메일 중복 확인
     *
     * @param email 중복 확인할 이메일
     * @return 중복 여부를 포함한 GlobalResponse 객체
     */
    @Override
    public GlobalResponse<CheckDuplicateEmailDto> checkDuplicateEmail(String email) {
        boolean existsByEmail = memberJpaRepository.existsByEmail(email);
        return new GlobalResponse<>(existsByEmail ? "DUPLICATED_EMAIL" : "SUCCESS",
                new CheckDuplicateEmailDto(existsByEmail));
    }

    /**
     * 닉네임 중복 확인
     *
     * @param nickname 중복 확인할 닉네임
     * @return 중복 여부를 포함한 GlobalResponse 객체
     */
    @Override
    public GlobalResponse<CheckDuplicateNicknameDto> checkDuplicateNickname(String nickname) {
        boolean existsByNickname = memberJpaRepository.existsByNickname(nickname);
        return new GlobalResponse<>(existsByNickname ? "DUPLICATED_NICKNAME" : "SUCCESS",
                new CheckDuplicateNicknameDto(existsByNickname));
    }

    /**
     * 회원 헤더 정보 조회
     *
     * @param id 회원 ID
     * @return 회원 헤더 정보를 포함한 GlobalResponse 객체
     */
    @Override
    public GlobalResponse<MemberHeaderInfoDto> getMemberHeaderInfo(Long id) {
        MemberEntity memberEntity = memberJpaRepository.findById(id)
                .orElseThrow(NotFoundMemberException::new);
        return new GlobalResponse<>(memberEntity.getNickname() == null ? "FIRST_LOGIN" : "SUCCESS",
                MemberHeaderInfoDto.fromEntity(memberEntity));
    }

    /**
     * 최초 로그인한 회원의 정보를 업데이트하고 JWT 토큰 반환
     *
     * @param tempTokenCookie 임시 토큰
     * @param request         업데이트할 회원 정보
     * @return 업데이트된 회원 정보와 JWT 토큰을 포함한 TokenAndHeaderInfoDto
     */
    @Override
    public TokenAndHeaderInfoDto getFirstUpdateMemberResponse(String tempTokenCookie,
                                                              FirstUpdateMemberRequest request) {
        if (!jwtProvider.validateToken(tempTokenCookie)) {
            throw new GlobalException(ErrorCode.INVALID_TOKEN);
        }

        if (memberJpaRepository.existsByNickname(request.nickname())) {
            throw new DuplicateMemberNicknameException();
        }

        // 임시 토큰에서 이메일과 프로필 이미지를 추출
        MemberEmailAndProfileImagePathDto memberProfileImagePathDto = jwtProvider.getMemberProfileImagePathDtoFromToken(
                tempTokenCookie);

        // 새로운 회원 엔티티 생성 및 저장
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

        // JWT 토큰 생성
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

    /**
     * 회원 프로필 조회 (최신 3개의 리뷰 포함)
     *
     * @param memberId 회원 ID
     * @return 회원의 프로필 정보와 최신 리뷰를 포함한 ProfileDto
     */
    @Override
    public ProfileDto getProfile(Long memberId) {
        MemberEntity member = memberJpaRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        // 전체 리뷰 수 가져오기
        int reviewCount = accompanyReviewRepository.countByTargetId(memberId);

        // 최신 3개의 리뷰 가져오기
        List<AccompanyReviewResponse> recentReviews = accompanyReviewRepository.findRecentReviewsByTargetId(memberId)
                .stream()
                .map(review -> AccompanyReviewResponse.fromEntity(review, reviewCount)) // 리뷰 수를 함께 전달
                .collect(Collectors.toList());

        // 나이 계산 및 나이대 결정
        int age = calculateAge(member.getBirthDate().toLocalDate());
        String ageGroup = calculateAgeGroup(age);

        return ProfileDto.fromEntity(member, recentReviews, reviewCount, ageGroup);
    }


    /**
     * 특정 회원이 받은 모든 리뷰를 조회하고, 각 리뷰를 AccompanyReviewResponse로 변환하여 반환하는 메서드.
     *
     * @param memberId 리뷰 대상이 되는 회원 ID
     * @return 해당 회원이 받은 모든 리뷰 목록과 리뷰 대상자가 받은 전체 리뷰 수를 포함한 리스트
     * @
     */
    @Override
    public AccompanyReviewResponse2 getAllReviews(Long memberId) {
        // 전체 리뷰 목록을 조회
        MemberEntity memberEntity = memberJpaRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        List<AccompanyReviewEntity> reviews = accompanyReviewRepository.findAllByTargetIdOrderByCreatedAtDesc(memberId);

        List<AccompanyReviewDto> AccompanyReviewDto = reviews.stream()
                .map(accompanyReviewEntity -> new AccompanyReviewDto(
                        accompanyReviewEntity.getId(),
                        accompanyReviewEntity.getReviewer().getId(),
                        accompanyReviewEntity.getReviewer().getNickname(),
                        accompanyReviewEntity.getReviewer().getProfileImagePath(),
                        accompanyReviewEntity.getContent(),
                        formatToUTC(accompanyReviewEntity.getCreatedAt())
                ))
                .toList();

        return new AccompanyReviewResponse2(
                memberId,
                memberEntity.getNickname(),
                reviews.size(),
                AccompanyReviewDto
        );
    }

    // todo-noah: 추후 UTC 관련 utils로 분리
    private final DateTimeFormatter UTC_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private String formatToUTC(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.atZone(ZoneId.systemDefault()) // 시스템 시간대 적용
                .format(UTC_FORMATTER); // 형식에 맞춰 반환
    }

    /**
     * 나이대 계산
     *
     * @param age 나이
     * @return 나이대에 해당하는 문자열
     */
    public String calculateAgeGroup(int age) {
        return AgeGroup.fromAge(age);
    }

    /**
     * 나이 계산 메서드
     *
     * @param birthDate 생년월일
     * @return 계산된 나이
     */
    private int calculateAge(LocalDate birthDate) {
        LocalDate now = LocalDate.now();
        return Period.between(birthDate, now).getYears();
    }

    /**
     * 회원 프로필 수정 (닉네임과 자기소개 수정 가능)
     *
     * @param memberId 회원 ID
     * @param dto      수정할 프로필 정보 (닉네임, 자기소개)
     */
    @Override
    public void updateProfile(Long memberId, ProfileUpdateRequestDto dto) {
        // MemberEntity 조회
        MemberEntity member = memberJpaRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        String nickname =
                dto.getNickname() != null && !dto.getNickname().isEmpty() ? dto.getNickname() : member.getNickname();
        String description = dto.getDescription() != null && !dto.getDescription().isEmpty() ? dto.getDescription()
                : member.getDescription();

        member.profileUpdate(nickname, description);

        memberJpaRepository.save(member);
    }
}
