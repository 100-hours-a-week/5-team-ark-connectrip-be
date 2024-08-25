package connectripbe.connectrip_be.chat.entity.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChatRoomType {

      ACTIVE("ACTIVE"),
      FINISH("FINISH"),
      DELETE("DELETE")
      ;

      private final String code;
}
