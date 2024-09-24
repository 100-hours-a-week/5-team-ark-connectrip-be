package connectripbe.connectrip_be.auth.web;

import connectripbe.connectrip_be.auth.dto.SignInDto;
import connectripbe.connectrip_be.auth.dto.SignUpDto;
import connectripbe.connectrip_be.auth.exception.RedirectFailureException;
import connectripbe.connectrip_be.auth.jwt.dto.TokenDto;
import connectripbe.connectrip_be.auth.kakao.service.KakaoService;
import connectripbe.connectrip_be.auth.service.AuthService;
import connectripbe.connectrip_be.global.dto.GlobalResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    // private final MailService mailService;
    private final KakaoService kakaoService;

    @Value("${spring.auth.success-redirect-url}")
    private String authSuccessRedirectUrl;

    @Value("${spring.auth.first-login-redirect-url}")
    private String kakaoFirstLoginRedirectUrl;

    @GetMapping("/redirected/kakao")
    public void kakaoLogin(
            HttpServletResponse httpServletResponse,
            @RequestParam("code") String code
    ) {
        TokenDto tokenDto = kakaoService.kakaoLogin(code);

        try {
            if (tokenDto.isFirstLogin()) {
                Cookie tempTokenCookie = new Cookie("tempToken", tokenDto.getTempToken());
                tempTokenCookie.setPath("/");
                tempTokenCookie.setMaxAge(tokenDto.getTempTokenExpirationTime());
                tempTokenCookie.setHttpOnly(true);

                httpServletResponse.addCookie(tempTokenCookie);

                httpServletResponse.sendRedirect(kakaoFirstLoginRedirectUrl);
            } else {
                addJwtToCookie(httpServletResponse, tokenDto);

                httpServletResponse.sendRedirect(authSuccessRedirectUrl);
            }
        } catch (IOException e) {
            throw new RedirectFailureException();
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

    @PostMapping(path = "/signup")
    public ResponseEntity<SignUpDto> signUp(
            @RequestPart(name = "request") SignUpDto request,
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
        refreshTokenCookie.setHttpOnly(true);

        response.addCookie(refreshTokenCookie);

        Cookie accessTokenCookie = new Cookie("accessToken", tokenDto.getAccessToken());
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(tokenDto.getAccessTokenExpirationTime());
        accessTokenCookie.setHttpOnly(true);

        response.addCookie(accessTokenCookie);
    }

    // fixme-noah, 2024-08-24: 예외에 대한 응답이 명확해지면 수정
    @ExceptionHandler(RedirectFailureException.class)
    public ResponseEntity<GlobalResponse<Void>> handleRedirectFailureException(RedirectFailureException e) {
        return new ResponseEntity<>(new GlobalResponse<>("REDIRECT_FAILURE", null), e.getErrorCode().getHttpStatus());
    }
}
