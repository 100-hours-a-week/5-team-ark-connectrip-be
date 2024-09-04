package connectripbe.connectrip_be.auth.kakao.service;

import connectripbe.connectrip_be.auth.jwt.dto.TokenDto;
import connectripbe.connectrip_be.auth.kakao.config.KakaoApi;
import connectripbe.connectrip_be.auth.kakao.dto.api.KakaoTokenApiResponse;
import connectripbe.connectrip_be.auth.kakao.dto.api.KakaoUserInfoApiResponse;
import connectripbe.connectrip_be.auth.service.AuthService;
import connectripbe.connectrip_be.global.exception.GlobalException;
import connectripbe.connectrip_be.global.exception.type.ErrorCode;
import connectripbe.connectrip_be.member.entity.MemberEntity;
import connectripbe.connectrip_be.member.repository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoService {

    private final KakaoApi kakaoApi;
    private final MemberJpaRepository memberJpaRepository;
    private final AuthService authService;

    @Value("${spring.auth.kakao.client-id}")
    private String clientId;

    @Value("${spring.auth.kakao.redirect-uri}")
    private String redirectUri;

    public TokenDto kakaoLogin(String code) {
        // 카카오 서버로부터 액세스 토큰을 받아온다.
        KakaoTokenApiResponse token = kakaoApi.getToken("authorization_code", clientId,
                redirectUri,
                code);

        String accessToken = token.getAccess_token();

        // 받아온 토큰을 사용하여 사용자 정보를 요청한다.
        KakaoUserInfoApiResponse userInfo = kakaoApi.getUserInfo("Bearer " + accessToken);

        try {
            // 이메일을 통해 기존 회원이 있는지 확인한다.
            MemberEntity memberEntity = memberJpaRepository.findByEmail(
                            userInfo.getKakaoAccount().getEmail())
                    .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_MEMBER));

            // 토큰 생성 및 반환
            return authService.generateToken(memberEntity.getId());
        } catch (GlobalException e) {

            // 카카오로 처음 로그인(회원가입) 하는 경우, 사용자 정보를 반환하여 추가 정보 입력을 유도한다.

            if (ErrorCode.NOT_FOUND_MEMBER.equals(e.getErrorCode())) {
                String memberEmail = userInfo.getKakaoAccount().getEmail();
                String memberProfileImagePath = getProfileImagePath(
                        userInfo.getKakaoAccount().getKakaoProfile().getImagePath());

                return authService.generateKaKaoTempToken(memberEmail, memberProfileImagePath);
            } else {
                throw e; // USER_NOT_FOUND 가 아닌 다른 GlobalException 은 그대로 다시 throw.
            }
        }
    }

    //  카카오에서 이미지를 받아오면 https://k.kakaocdn.net 형식만 받아온다.
    //  나머지 형식이면 null로 받아온다.

    /**
     * 카카오에서  프로필 이미지를 받아오면 k.kakaocdn.net 형식만 받아온다. 나머지 형식이면 null 반환.
     *
     * @param imagePath 카카오에서 받아온 이미지 경로
     * @return 카카오 이미지 경로
     */
    private String getProfileImagePath(String imagePath) {
        if (imagePath != null && imagePath.contains("k.kakaocdn.net")) {
            return imagePath;
        }
        return null;
    }
}
