package connectripbe.connectrip_be.auth.kakao.service;




import static connectripbe.connectrip_be.global.exception.type.ErrorCode.USER_NOT_FOUND;

import connectripbe.connectrip_be.auth.jwt.dto.TokenDto;
import connectripbe.connectrip_be.auth.kakao.config.KakaoApi;
import connectripbe.connectrip_be.auth.kakao.dto.KaKaoSignUpDto;
import connectripbe.connectrip_be.auth.kakao.dto.api.KakaoTokenApiResponse;
import connectripbe.connectrip_be.auth.kakao.dto.api.KakaoUserInfoApiResponse;
import connectripbe.connectrip_be.auth.service.AuthService;
import connectripbe.connectrip_be.global.exception.GlobalException;
import connectripbe.connectrip_be.member.entity.Member;
import connectripbe.connectrip_be.member.entity.type.MemberRoleType;
import connectripbe.connectrip_be.member.repository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;



@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {

  private final KakaoApi kakaoApi;
  private final MemberJpaRepository memberJpaRepository;
  private final AuthService authService;

  @Value("${spring.data.kakao.grant_type}")
  private  String grant_type;

  @Value("${spring.data.kakao.client_id}")
  private String client_id;

  @Value("${spring.data.kakao.redirect_uri}")
  private String redirect_uri;

  /**
   * 카카오 로그인 처리 메서드.
   *
   * @param code 카카오 로그인 인증 후 받은 코드
   * @return 로그인 처리 결과
   */

  public Object kakaoLogin(String code) {
    // 카카오 서버로부터 액세스 토큰을 받아온다.
    KakaoTokenApiResponse token = kakaoApi.getToken(grant_type, client_id, redirect_uri, code);
    String accessToken = token.getAccess_token();

    // 받아온 토큰을 사용하여 사용자 정보를 요청한다.
    KakaoUserInfoApiResponse userInfo = kakaoApi.getUserInfo("Bearer " + accessToken);

    try {
      // 이메일을 통해 기존 회원이 있는지 확인한다.
      Member member = memberJpaRepository.findByEmail(userInfo.getKakaoAccount().getEmail())
          .orElseThrow(() -> new GlobalException(USER_NOT_FOUND));


      // 토큰 생성 및 반환
      return authService.generateToken(member.getEmail(), member.getRoleType().getCode());

    } catch (GlobalException e) {
      // 카카오로 처음 로그인(회원가입) 하는 경우, 사용자 정보를 반환하여 추가 정보 입력을 유도한다.
      if (USER_NOT_FOUND.equals(e.getErrorCode())) {
        Member newMember = Member.builder()
                .email(userInfo.getKakaoAccount().getEmail())
                .roleType(MemberRoleType.USER) // 기본 사용자 권한 설정
                .build();
        memberJpaRepository.save(newMember);

        // 토큰 생성 및 반환
        return authService.generateToken(newMember.getEmail(), newMember.getRoleType().getCode());

      } else {
        throw e; // USER_NOT_FOUND 가 아닌 다른 GlobalException 은 그대로 다시 throw.
      }
    }
  }

  /**
   * 카카오 회원가입 처리 메서드.
   *
   * @param request 회원가입 요청 정보를 담은 DTO
   * @return 생성된 토큰 정보
   */

  public TokenDto kakaoSignUp(KaKaoSignUpDto request) {

    // DTO 로부터 Member 엔티티 생성
    Member member = KaKaoSignUpDto.toEntity(request);

    // 회원 정보 저장
    memberJpaRepository.save(member);

    // 토큰 생성 및 반환
    return authService.generateToken(member.getEmail(), member.getRoleType().getCode());
  }

}

