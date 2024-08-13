package connectripbe.connectrip_be.auth.jwt;


import static connectripbe.connectrip_be.global.exception.type.ErrorCode.*;

import connectripbe.connectrip_be.auth.jwt.dto.TokenDto;
import connectripbe.connectrip_be.global.exception.GlobalException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


/**
 * JWT 토큰을 생성하고 검증
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

      @Value("${spring.jwt.secret}")
      private String secretKey;

      @Value("${spring.jwt.access-token-expire-time}")
      private Long accessTokenExpiration;

      @Value("${spring.jwt.refresh-token-expire-time}")
      private Long refreshTokenExpiration;

      private static final String KEY_ROLES = "roles";
      private static final String USER_ID = "userId";

      private final JwtUserDetailService userDetailService;


      /**
       * 주어진 이메일과 roleType(권한)에 대해 엑세스 토큰과 리프레시 토큰 생성
       *
       * @param email    사요자 이메일 주소
       * @param roleType 사용자 유형
       * @return 생성된 엑세스 토큰과 리프레시 토큰 정보가 담긴 TokenDto 객체
       */
      public TokenDto generateToken(String email, String roleType) {
            Date now = new Date();

            Date accessTokenExpireTime = new Date(now.getTime() + accessTokenExpiration);
            Date refreshTokenExpireTime = new Date(now.getTime() + refreshTokenExpiration);

            String accessToken = Jwts.builder()
                    .setSubject("access-token")
                    .claim(KEY_ROLES, roleType)
                    .claim(USER_ID, email)
                    .setIssuedAt(now) // 토큰 생성 시간
                    .setExpiration(accessTokenExpireTime) // 토큰 만료 시간
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256) // 사용할 암호화 알고리즘 비밀키
                    .compact();
            log.info("Email stored in accessToken: {}", email);

            String refreshToken = Jwts.builder()
                    .setSubject("refresh-token")
                    .claim(KEY_ROLES, roleType)
                    .claim(USER_ID, email)
                    .setIssuedAt(now) // 토큰 생성 시간
                    .setExpiration(refreshTokenExpireTime) // 토큰 만료 시간
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256) // 사용할 암호화 알고리즘 비밀키
                    .compact();

            return TokenDto.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .accessTokenExpireTime(accessTokenExpireTime.getTime())
                    .refreshTokenExpireTime(refreshTokenExpireTime.getTime())
                    .build();
      }

      /**
       * 주어진 토큰이 유효한지 검증. 토큰이 유효하지 않은 경우, 예외 발생
       *
       * @param token 검증할 JWT 토큰
       * @return 토큰의 유효성 검증 결과(유효할 경우 true, 그렇지 않을 경우 false)
       */
      public boolean validateToken(String token) {
            try {
                  Claims claims = this.parseClaims(token);
                  return !claims.getExpiration()
                          .before(new Date()); // 토큰의 만료시간이 현재의 시간보다 이전인지 아닌지 확인
            } catch (SecurityException | MalformedJwtException e) {
                  log.error("Invalid JWT token: {}", e.getMessage());
                  throw new GlobalException(INVALID_TOKEN);
            } catch (ExpiredJwtException e) {
                  log.error("JWT token is expired: {}", e.getMessage());
                  throw new GlobalException(EXPIRED_TOKEN);
            } catch (UnsupportedJwtException e) {
                  log.error("JWT token is unsupported: {}", e.getMessage());
                  throw new GlobalException(UNSUPPORTED_TOKEN);
            } catch (IllegalArgumentException e) {
                  log.error("JWT token is wrong type: {}", e.getMessage());
                  throw new GlobalException(WRONG_TYPE_TOKEN);
            }
      }

      /**
       * 주어진 토큰에 대한 인증 정보를 반환
       *
       * @param token 인증 정보를 추출할 JWT 토큰
       * @return 토큰에 담긴 인증 정보 (Authentication 객체)
       */
      public Authentication getAuthentication(String token) {
            Claims claims = this.parseClaims(token);
            log.info("Claims object: {}", claims);

            String userId = claims.get(USER_ID).toString();
            log.info("Extracted userId from token: {}", userId);

            JwtUserDetails userDetails = (JwtUserDetails) userDetailService
                    .loadUserByUsername(userId);
            log.info("UserDetails object: {}", userDetails);

            return new UsernamePasswordAuthenticationToken(userDetails, "",
                    userDetails.getAuthorities());
      }



      /**
       * 주어진 토큰을 파싱하여 Claims 객체로 반환
       *
       * @return 토큰의 Claims 객체
       */
      private Claims parseClaims(String token) {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
      }


      /**
       * Base64 인코딩된 secretKey 를 반환
       */
      private Key getSigningKey() {
            String encoded = Base64.getEncoder().encodeToString(
                    secretKey.getBytes(StandardCharsets.UTF_8));

            return Keys.hmacShaKeyFor(encoded.getBytes());
      }

      /**
       *  주어진 토큰 만료 시간을 반환
       */

      public Long getExpireTime(String token){
            return this.parseClaims(token).getExpiration().getTime();
      }
}
