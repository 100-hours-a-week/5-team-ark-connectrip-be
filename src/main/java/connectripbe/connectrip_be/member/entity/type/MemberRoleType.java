package connectripbe.connectrip_be.member.entity.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberRoleType {

      USER("ROLE_USER"),
      ADMIN("ROLE_ADMIN");

      private final String code;
}
