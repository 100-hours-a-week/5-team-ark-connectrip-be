package connectripbe.connectrip_be.auth.kakao.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class KakaoUserInfoApiResponse {

  @JsonProperty("id")
  private Long id;

  @JsonProperty("kakao_account")
  private KakaoAccount kakaoAccount;


}
