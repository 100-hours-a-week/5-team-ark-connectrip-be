package connectripbe.connectrip_be.auth.service;

import connectripbe.connectrip_be.auth.dto.SignInDto;
import connectripbe.connectrip_be.auth.dto.SignUpDto;
import connectripbe.connectrip_be.auth.jwt.JwtProvider;
import connectripbe.connectrip_be.auth.jwt.dto.TokenDto;
import connectripbe.connectrip_be.global.exception.GlobalException;
import connectripbe.connectrip_be.global.exception.type.ErrorCode;
import connectripbe.connectrip_be.global.util.aws.service.AwsS3Service;
import connectripbe.connectrip_be.member.entity.MemberEntity;
import connectripbe.connectrip_be.member.repository.MemberJpaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final MemberJpaRepository memberJpaRepository;

    private final AwsS3Service awsS3Service;
    private final JwtProvider jwtProvider;

    @Transactional
    public SignUpDto signUp(SignUpDto request, MultipartFile profileImage) {
        if (memberJpaRepository.existsByEmail(request.getEmail())) {
            throw new GlobalException(ErrorCode.DUPLICATE_USER);
        }
        String encodedPasswordEncoder = passwordEncoder.encode(request.getPassword());

        // 프로필 이미지가 제공되었을 경우 S3에 업로드하고 그 URL 을 가져옴
        String profileImageUrl = null; // 기본값은 null
        if (profileImage != null && !profileImage.isEmpty()) {
            // 이미지 업로드 시도
            try {
                profileImageUrl = uploadProfileImage(profileImage);  // 수정
            } catch (Exception e) { // 이미지 업로드 중 오류 발생 시 예외 처리
                throw new GlobalException(ErrorCode.PROFILE_IMAGE_UPLOAD_ERROR);
            }
        }

        request.setProfileImageUrl(profileImageUrl);  // 새로 추가

        MemberEntity memberEntityToSave = SignUpDto.signUpForm(request, encodedPasswordEncoder);

        return SignUpDto.fromEntity(memberJpaRepository.save(memberEntityToSave));
    }

    public TokenDto signIn(SignInDto request) {
        MemberEntity memberEntity = memberJpaRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), memberEntity.getPassword())) {
            throw new GlobalException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        return generateToken(memberEntity.getId());
    }

    @Override
    public TokenDto generateToken(long memberId) {
        String refreshToken = jwtProvider.generateRefreshToken(memberId);
        String accessToken = jwtProvider.generateAccessToken(memberId);

        // info-noah: 만료 시간이 밀리초로 설정되어 있기 때문에 1000을 나눔
        return TokenDto.builder()
                .refreshToken(refreshToken)
                .refreshTokenExpirationTime(jwtProvider.getRefreshTokenExpirationTime() / 1000)
                .accessToken(accessToken)
                .accessTokenExpirationTime(jwtProvider.getAccessTokenExpirationTime() / 1000)
                .build();
    }

    /**
     * 프로필 이미지를 AWS S3에 업로드하고 그에 대한 URL 을 반환.
     *
     * @param multipartFile 업로드할 이미지 파일
     * @return 업로드된 이미지의 S3 URL
     */
    private String uploadProfileImage(MultipartFile multipartFile) {
        String fileName = awsS3Service.generateFileName(multipartFile);
        awsS3Service.uploadToS3(multipartFile, fileName);

        return awsS3Service.getUrl(fileName);
    }
}
