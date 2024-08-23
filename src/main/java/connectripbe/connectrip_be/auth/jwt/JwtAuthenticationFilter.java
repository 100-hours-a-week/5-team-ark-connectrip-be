package connectripbe.connectrip_be.auth.jwt;


import connectripbe.connectrip_be.auth.jwt.token.JwtAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();

        if (cookies == null || cookies.length == 0) {
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = null;
        String accessToken = null;

        for (Cookie cookie : cookies) {
            if ("refreshToken".equals(cookie.getName())) {
                refreshToken = cookie.getValue();
            }

            if ("accessToken".equals(cookie.getName())) {
                accessToken = cookie.getValue();
            }
        }

        if (refreshToken == null || accessToken == null || refreshToken.isBlank() || accessToken.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!jwtProvider.validateToken(accessToken)) {
            if (!jwtProvider.validateToken(refreshToken)) {
                filterChain.doFilter(request, response);
                return;
            }

            long memberId = jwtProvider.getMemberIdFromToken(refreshToken);

            accessToken = jwtProvider.generateAccessToken(memberId);

            Cookie newAccessTokenCookie = new Cookie("accessToken", accessToken);
            newAccessTokenCookie.setPath("/");
            newAccessTokenCookie.setMaxAge(jwtProvider.getAccessTokenExpirationTime() / 1000);

            response.addCookie(newAccessTokenCookie);
        }

        long memberId = jwtProvider.getMemberIdFromToken(accessToken);

        SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(memberId));

        filterChain.doFilter(request, response);
    }
}
