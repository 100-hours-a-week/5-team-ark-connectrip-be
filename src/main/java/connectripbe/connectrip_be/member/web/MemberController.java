package connectripbe.connectrip_be.member.web;

import connectripbe.connectrip_be.auth.jwt.dto.TokenDto;
import connectripbe.connectrip_be.global.dto.GlobalResponse;
import connectripbe.connectrip_be.member.dto.CheckDuplicateEmailDto;
import connectripbe.connectrip_be.member.dto.CheckDuplicateNicknameDto;
import connectripbe.connectrip_be.member.dto.FirstUpdateMemberRequest;
import connectripbe.connectrip_be.member.dto.MemberHeaderInfoDto;
import connectripbe.connectrip_be.member.dto.ProfileDto;
import connectripbe.connectrip_be.member.dto.ProfileUpdateRequestDto;
import connectripbe.connectrip_be.member.dto.TokenAndHeaderInfoDto;
import connectripbe.connectrip_be.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 이메일 중복 확인 메서드
     *
     * @param email 중복 확인할 이메일
     * @return 이메일 중복 여부를 담은 GlobalResponse
     */
    @GetMapping("/check-email")
    public GlobalResponse<CheckDuplicateEmailDto> checkDuplicateEmail(
            @RequestParam String email
    ) {
        return memberService.checkDuplicateEmail(email);
    }

    /**
     * 닉네임 중복 확인 메서드
     *
     * @param nickname 중복 확인할 닉네임
     * @return 닉네임 중복 여부를 담은 GlobalResponse
     */
    @GetMapping("/check-nickname")
    public GlobalResponse<CheckDuplicateNicknameDto> checkDuplicateNickname(
            @RequestParam String nickname
    ) {
        return memberService.checkDuplicateNickname(nickname);
    }

    /**
     * 현재 로그인한 회원의 헤더 정보를 반환
     *
     * @param id 현재 로그인한 회원의 ID (AuthenticationPrincipal 사용)
     * @return 회원의 헤더 정보를 담은 GlobalResponse
     */
    @GetMapping("/me")
    public GlobalResponse<MemberHeaderInfoDto> getMemberHeaderInfo(
            @AuthenticationPrincipal Long id
    ) {
        return memberService.getMemberHeaderInfo(id);
    }

    /**
     * 회원의 프로필 정보 조회
     *
     * @param memberId 조회할 회원의 ID
     * @return 회원의 프로필과 최신 3개 리뷰를 담은 ProfileDto
     */
    @GetMapping("/profile/{memberId}")
    public ResponseEntity<ProfileDto> getProfile(@PathVariable Long memberId) {
        ProfileDto profile = memberService.getProfile(memberId);
        return ResponseEntity.ok(profile);
    }

    /**
     * 회원 프로필 수정 (닉네임 및 자기소개 수정 가능)
     *
     * @param memberId 수정할 회원의 ID
     * @param dto      수정할 프로필 정보 (닉네임, 자기소개)
     * @return 200 OK 응답 (수정된 프로필 정보를 반환하지 않음)
     */
    @PostMapping("/{memberId}/profile")
    public ResponseEntity<Void> updateProfile(@PathVariable Long memberId,
                                              @RequestBody ProfileUpdateRequestDto dto) {
        memberService.updateProfile(memberId, dto);
        return ResponseEntity.ok().build();
    }


    /**
     * 최초 로그인한 회원의 정보를 업데이트하고 JWT 토큰을 반환
     *
     * @param tempTokenCookie     임시 토큰 (쿠키에서 가져옴)
     * @param request             업데이트할 회원 정보
     * @param httpServletRequest  HTTP 요청 객체
     * @param httpServletResponse HTTP 응답 객체
     * @return 업데이트된 회원 헤더 정보를 담은 GlobalResponse
     */
    @PostMapping("/first")
    public ResponseEntity<GlobalResponse<MemberHeaderInfoDto>> firstUpdateMember(
            @CookieValue(value = "tempToken") String tempTokenCookie,
            @RequestBody FirstUpdateMemberRequest request,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse
    ) {
        TokenAndHeaderInfoDto tokenAndHeaderInfoDto = memberService.getFirstUpdateMemberResponse(tempTokenCookie,
                request);

        // tempToken 만료
        Cookie[] cookies = httpServletRequest.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookie.setMaxAge(0);  // 쿠키 만료 처리
                cookie.setPath("/");
                httpServletResponse.addCookie(cookie);
            }
        }

        // 새로운 JWT 토큰을 쿠키에 추가
        addJwtToCookie(httpServletResponse, tokenAndHeaderInfoDto.tokenDto());

        return ResponseEntity
                .status(200)
                .body(new GlobalResponse<>("SUCCESS", tokenAndHeaderInfoDto.memberHeaderInfoDto()));
    }

    /**
     * JWT 토큰을 쿠키에 추가하는 메서드
     *
     * @param response HTTP 응답 객체
     * @param tokenDto JWT 토큰 정보
     */
    private void addJwtToCookie(
            HttpServletResponse response,
            TokenDto tokenDto
    ) {
        // Refresh Token 설정
        Cookie refreshTokenCookie = new Cookie("refreshToken", tokenDto.getRefreshToken());
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(tokenDto.getRefreshTokenExpirationTime());
        refreshTokenCookie.setHttpOnly(true);

        response.addCookie(refreshTokenCookie);

        // Access Token 설정
        Cookie accessTokenCookie = new Cookie("accessToken", tokenDto.getAccessToken());
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(tokenDto.getAccessTokenExpirationTime());
        accessTokenCookie.setHttpOnly(true);

        response.addCookie(accessTokenCookie);
    }
}
