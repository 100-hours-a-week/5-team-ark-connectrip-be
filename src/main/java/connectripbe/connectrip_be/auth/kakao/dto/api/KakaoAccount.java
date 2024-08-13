package connectripbe.connectrip_be.auth.kakao.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class KakaoAccount {

  @JsonProperty("name")
  String name;

  @JsonProperty("email")
  String email;

  @JsonProperty("gender")
  String gender;

}
