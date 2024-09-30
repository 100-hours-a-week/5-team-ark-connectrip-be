package connectripbe.connectrip_be.accompany.status.response;

import connectripbe.connectrip_be.accompany.status.entity.AccompanyStatusEnum;

public record AccompanyStatusResponse(
        long accompanyPostId,
        AccompanyStatusEnum accompanyStatus
) {
}
