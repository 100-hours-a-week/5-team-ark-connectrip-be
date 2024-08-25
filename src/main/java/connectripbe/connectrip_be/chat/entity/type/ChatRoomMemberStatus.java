package connectripbe.connectrip_be.chat.entity.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChatRoomMemberStatus {
      ACTIVE("활성"),
      EXIT("나감");


      private final String message;
}
