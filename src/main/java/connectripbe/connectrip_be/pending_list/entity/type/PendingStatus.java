package connectripbe.connectrip_be.pending_list.entity.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PendingStatus {

    PENDING("대기중"),
    ACCEPTED("수락"),
    REJECTED("거절"),
    EXIT_ROOM("나감")
    ;

    private final String message;
}
