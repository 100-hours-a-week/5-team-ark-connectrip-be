package connectripbe.connectrip_be.accompany_status.response;

import connectripbe.connectrip_be.accompany_status.entity.AccompanyStatusEnum;

public record AccompanyStatusResponse(
        long accompanyPostId,
        AccompanyStatusEnum accompanyStatus
) {
}
