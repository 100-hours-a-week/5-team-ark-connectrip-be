package connectripbe.connectrip_be.auth.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// fixme-noah 2024-09-04 : TokenDto 분리 필요

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {
    private String refreshToken;
    private int refreshTokenExpirationTime;
    private String accessToken;
    private int accessTokenExpirationTime;

    private boolean isFirstLogin;
    private String tempToken;
    private int tempTokenExpirationTime;
}
