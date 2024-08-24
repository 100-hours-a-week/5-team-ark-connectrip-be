package connectripbe.connectrip_be.pending_list.dto;

import connectripbe.connectrip_be.pending_list.entity.type.PendingStatus;
import lombok.Builder;

@Builder
public record PendingListResponse(
        String status
) {

}
