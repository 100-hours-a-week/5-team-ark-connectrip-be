package connectripbe.connectrip_be.pending_list.entity.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PendingStatus {

    PENDING("대기중"),
    ACCEPT("수락"),
    REJECT("거절");

    private final String message;
}
