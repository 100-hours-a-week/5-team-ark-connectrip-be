package connectripbe.connectrip_be.member.entity.type;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberLoginType {

  EMAIL("LOGIN_EMAIL"),
  KAKAO("LOGIN_KAKAO"),
  ;
  private final String code;
}
