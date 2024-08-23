package connectripbe.connectrip_be.auth.web;

import connectripbe.connectrip_be.auth.dto.SignInDto;
import connectripbe.connectrip_be.auth.dto.SignUpDto;
import connectripbe.connectrip_be.auth.jwt.dto.TokenDto;
import connectripbe.connectrip_be.auth.kakao.service.KakaoService;
import connectripbe.connectrip_be.auth.service.AuthService;
import connectripbe.connectrip_be.global.dto.GlobalResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    // private final MailService mailService;
    private final KakaoService kakaoService;

    @Value("${spring.auth.success-redirect-url}")
    private String authSuccessRedirectUrl;

    @GetMapping("/redirected/kakao")
    public void kakaoLogin(
            HttpServletResponse httpServletResponse,
            @RequestParam("code") String code
    ) {
        TokenDto tokenDto = kakaoService.kakaoLogin(code);

        addJwtToCookie(httpServletResponse, tokenDto);

        // fixme-noah: exception, 핸들러 추가
        try {
            httpServletResponse.sendRedirect(authSuccessRedirectUrl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping(path = "/signin")
    public ResponseEntity<Void> signIn(
            HttpServletResponse httpServletResponse,
            @RequestBody SignInDto request
    ) {
        TokenDto tokenDto = authService.signIn(request);

        addJwtToCookie(httpServletResponse, tokenDto);

        return ResponseEntity.ok().build();
    }

    // fixme-noah: 엔티티에 맞게 request 필드 추가 후 이름 변경
    @PostMapping(path = "/signup")
    public ResponseEntity<SignUpDto> signUp(
            SignUpDto request,
            @RequestPart(name = "image", required = false) MultipartFile image
    ) {
        authService.signUp(request, image);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse
    ) {
        Cookie[] cookies = httpServletRequest.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookie.setMaxAge(0);
                cookie.setPath("/");
                httpServletResponse.addCookie(cookie);
            }
        }

        return ResponseEntity.ok(new GlobalResponse<>("SUCCESS", null));
    }

    private void addJwtToCookie(
            HttpServletResponse response,
            TokenDto tokenDto
    ) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", tokenDto.getRefreshToken());
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(tokenDto.getRefreshTokenExpirationTime());

        response.addCookie(refreshTokenCookie);

        Cookie accessTokenCookie = new Cookie("accessToken", tokenDto.getAccessToken());
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(tokenDto.getAccessTokenExpirationTime());

        response.addCookie(accessTokenCookie);
    }
}
