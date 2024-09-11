package connectripbe.connectrip_be.member.web;

import connectripbe.connectrip_be.Review.dto.AccompanyReviewResponse;
import connectripbe.connectrip_be.auth.jwt.dto.TokenDto;
import connectripbe.connectrip_be.global.dto.GlobalResponse;
import connectripbe.connectrip_be.member.dto.CheckDuplicateEmailDto;
import connectripbe.connectrip_be.member.dto.CheckDuplicateNicknameDto;
import connectripbe.connectrip_be.member.dto.FirstUpdateMemberRequest;
import connectripbe.connectrip_be.member.dto.MemberHeaderInfoDto;
import connectripbe.connectrip_be.member.dto.ProfileDto;
import connectripbe.connectrip_be.member.dto.TokenAndHeaderInfoDto;
import connectripbe.connectrip_be.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
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

    @GetMapping("/check-email")
    public GlobalResponse<CheckDuplicateEmailDto> checkDuplicateEmail(
            @RequestParam String email
    ) {
        return memberService.checkDuplicateEmail(email);
    }

    @GetMapping("/check-nickname")
    public GlobalResponse<CheckDuplicateNicknameDto> checkDuplicateNickname(
            @RequestParam String nickname
    ) {
        return memberService.checkDuplicateNickname(nickname);
    }

    @GetMapping("/me")
    public GlobalResponse<MemberHeaderInfoDto> getMemberHeaderInfo(
            @AuthenticationPrincipal Long id
    ) {
        return memberService.getMemberHeaderInfo(id);
    }
    
    // 프로필 조회: 프로필 정보 및 최신 3개 리뷰 반환
    @GetMapping("/profile/{memberId}")
    public ResponseEntity<ProfileDto> getProfile(@PathVariable Long memberId) {
        ProfileDto profile = memberService.getProfile(memberId);
        return ResponseEntity.ok(profile);
    }

    // 모든 리뷰 조회: 다른 페이지로 이동해서 모든 리뷰를 반환
    @GetMapping("/profile/{memberId}/reviews")
    public ResponseEntity<List<AccompanyReviewResponse>> getAllReviews(@PathVariable Long memberId) {
        List<AccompanyReviewResponse> reviews = memberService.getAllReviews(memberId);
        return ResponseEntity.ok(reviews);
    }

    // fixme-noah: 2024-08-21, 엉망 코드
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
                cookie.setMaxAge(0);
                cookie.setPath("/");
                httpServletResponse.addCookie(cookie);
            }
        }

        addJwtToCookie(httpServletResponse, tokenAndHeaderInfoDto.tokenDto());

        return ResponseEntity
                .status(200)
                .body(new GlobalResponse<>("SUCCESS", tokenAndHeaderInfoDto.memberHeaderInfoDto()));
    }

    private void addJwtToCookie(
            HttpServletResponse response,
            TokenDto tokenDto
    ) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", tokenDto.getRefreshToken());
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(tokenDto.getRefreshTokenExpirationTime());
        refreshTokenCookie.setHttpOnly(true);

        response.addCookie(refreshTokenCookie);

        Cookie accessTokenCookie = new Cookie("accessToken", tokenDto.getAccessToken());
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(tokenDto.getAccessTokenExpirationTime());
        accessTokenCookie.setHttpOnly(true);

        response.addCookie(accessTokenCookie);
    }
}
