package connectripbe.connectrip_be.auth.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
