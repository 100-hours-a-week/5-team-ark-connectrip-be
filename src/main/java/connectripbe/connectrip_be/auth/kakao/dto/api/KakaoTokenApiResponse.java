package connectripbe.connectrip_be.auth.kakao.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class KakaoTokenApiResponse {

  @JsonProperty("token_type")
  String token_type;
  @JsonProperty("access_token")
  String access_token;
  @JsonProperty("expires_in")
  Integer expires_in;
  @JsonProperty("refresh_token")
  String refresh_token;
  @JsonProperty("scope")
  String scope;

}
