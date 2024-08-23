package connectripbe.connectrip_be.auth.web;

import connectripbe.connectrip_be.auth.dto.ReissueDto;
import connectripbe.connectrip_be.auth.dto.SignInDto;
import connectripbe.connectrip_be.auth.dto.SignUpDto;
import connectripbe.connectrip_be.auth.jwt.dto.TokenDto;
import connectripbe.connectrip_be.auth.kakao.service.KakaoService;
import connectripbe.connectrip_be.auth.service.AuthService;
import connectripbe.connectrip_be.global.dto.GlobalResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    // private final MailService mailService;
    private final KakaoService kakaoService;

    // fixme-noah: 자체 회원가입 도입 시 수정
    @PostMapping(path = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SignUpDto> signUp(@RequestPart("request") SignUpDto request,
                                            @RequestPart(name = "image", required = false) MultipartFile image) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.signUp(request, image));
    }

    @PostMapping(path = "/signin")
    public ResponseEntity<TokenDto> signIn(@RequestBody SignInDto request) {
        return ResponseEntity.ok(authService.signIn(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
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

    // fixme-noah: 임시 구현
    @GetMapping("/redirected/kakao")
    public void kakaoLogin(@RequestParam("code") String code, HttpServletResponse httpServletResponse) throws IOException {
        TokenDto tokenDto = kakaoService.kakaoLogin(code);

        Cookie refreshTokenCookie = new Cookie("refreshToken", tokenDto.getRefreshToken());
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(3600 * 24 * 7);

        httpServletResponse.addCookie(refreshTokenCookie);

        Cookie accessTokenCookie = new Cookie("accessToken", tokenDto.getAccessToken());
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(3600 * 24);

        httpServletResponse.addCookie(accessTokenCookie);

        // fixme-noah: 리다이렉트 url 환경 변수로 이동
        httpServletResponse.sendRedirect("http://localhost:3000/accompany");
    }
}
