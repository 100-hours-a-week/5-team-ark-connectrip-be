package connectripbe.connectrip_be.auth.jwt;

import connectripbe.connectrip_be.global.exception.GlobalException;
import connectripbe.connectrip_be.global.exception.type.ErrorCode;
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

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    @Value("${spring.jwt.secret-key}")
    private String jwtSecretKey;

    @Value("${spring.jwt.refresh-token-expiration-time}")
    @Getter
    private int refreshTokenExpirationTime;

    @Value("${spring.jwt.access-token-expiration-time}")
    @Getter
    private int accessTokenExpirationTime;

    public String generateRefreshToken(long memberId) {
        Date now = new Date();

        Date refreshTokenExpirationTime = new Date(now.getTime() + this.refreshTokenExpirationTime);

        String refreshToken = Jwts.builder()
                .setSubject(Long.toString(memberId))
                .claim("type", "refresh")
                .setIssuedAt(now)
                .setExpiration(refreshTokenExpirationTime)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

        return refreshToken;
    }

    public String generateAccessToken(long memberId) {
        Date now = new Date();

        Date accessTokenExpirationTime = new Date(now.getTime() + this.accessTokenExpirationTime);

        String accessToken = Jwts.builder()
                .setSubject("accessToken")
                .setSubject(Long.toString(memberId))
                .setIssuedAt(now)
                .setExpiration(accessTokenExpirationTime)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

        return accessToken;
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = this.parseClaims(token);
            return !claims.getExpiration().before(new Date()); // 토큰의 만료시간이 현재의 시간보다 이전인지 아닌지 확인
        } catch (SecurityException | MalformedJwtException e) {
            throw new GlobalException(ErrorCode.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new GlobalException(ErrorCode.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new GlobalException(ErrorCode.UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new GlobalException(ErrorCode.WRONG_TYPE_TOKEN);
        }
    }

    public long getMemberIdFromToken(String token) {
        Claims claims = parseClaims(token);

        return Long.parseLong(claims.getSubject());
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        String encoded = Base64.getEncoder().encodeToString(jwtSecretKey.getBytes(StandardCharsets.UTF_8));

        return Keys.hmacShaKeyFor(encoded.getBytes());
    }
}
