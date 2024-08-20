package connectripbe.connectrip_be.accompany_member.entity.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccompanyMemberStatus {

      ACTIVE("채팅방 활동 중"),
      EXIT("채팅방 나감");

    private final String displayName;
}
