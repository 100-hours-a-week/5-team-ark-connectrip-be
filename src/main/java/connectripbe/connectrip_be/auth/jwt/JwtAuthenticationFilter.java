package connectripbe.connectrip_be.auth.jwt;



import static connectripbe.connectrip_be.global.exception.type.ErrorCode.UNKNOWN_ERROR;

import connectripbe.connectrip_be.global.exception.GlobalException;
import connectripbe.connectrip_be.global.service.RedisService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


/**
 * 요청 헤더에서 JWT 토큰을 추출하고, 토큰의 유효성 검사 토큰이 유효하면 SecurityContext 에 인증 정보를 저장
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

      private final TokenProvider tokenProvider;
      private final RedisService redisService;

      @Value("${spring.jwt.prefix}")
      private String tokenPrefix;

      @Value("${spring.jwt.header}")
      private String tokenHeader;

      /**
       * HTTP 요청을 필터링 하여 JWT 토큰을 검증하고, 유효한 토큰일 경우 인증 정보를 SecurityContext 에 저장.
       * 만약 토큰이 유효하지 않거나 로그아웃된 상태라면 예외 처리
       *
       * @param request     HTTP 요청
       * @param response    HTTP 응답
       * @param filterChain 필터 체인
       * @throws ServletException 서블릿 예외
       * @throws IOException      입출력 예외
       */

      @Override
      protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
              FilterChain filterChain) throws ServletException, IOException {

            String token = resolveTokenFromRequest(request);

            try {

                  if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
                        // redis 에서 토큰에 대한 데이터가 없는 경우 (로그아웃 상태 아님)
                        if (Objects.isNull(redisService.getData(token))) {
                              // 토큰으로부터 인증 정보를 받아옴
                              Authentication authentication = tokenProvider.getAuthentication(token);
                              log.info("getAuthentication method called with token: {}", token);

                              // SecurityContext 에 인증 정보를 저장
                              SecurityContextHolder.getContext().setAuthentication(authentication);
                              log.info("Authentication object stored in SecurityContext: {}", authentication);

                        } else { // redis 에서 토큰에 대한 데이터가 있는 경우 (로그아웃 상태)
                              request.setAttribute("exception", UNKNOWN_ERROR);
                        }
                  }
            } catch (GlobalException e) {
                  request.setAttribute("exception", UNKNOWN_ERROR);
            } catch (Exception e) {
                  log.error(e.getMessage());
            }

            filterChain.doFilter(request, response);
      }

      /**
       * HTTP 요청 헤더에서 JWT 토큰 추출하여 반납.
       *
       * @param request HTTP 요청
       * @return 추출한 JWT 토큰 or null
       */
      private String resolveTokenFromRequest(HttpServletRequest request) {
            String token = request.getHeader(this.tokenHeader);
            if (!ObjectUtils.isEmpty(token) && token.startsWith(this.tokenPrefix)) {
                  return token.substring(this.tokenPrefix.length());
            }
            return null;
      }
}
