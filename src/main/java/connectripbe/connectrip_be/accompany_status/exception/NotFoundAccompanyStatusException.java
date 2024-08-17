package connectripbe.connectrip_be.accompany_status.exception;

import connectripbe.connectrip_be.global.exception.GlobalException;
import connectripbe.connectrip_be.global.exception.type.ErrorCode;

public class NotFoundAccompanyStatusException extends GlobalException {

    public NotFoundAccompanyStatusException() {
        super(ErrorCode.NOT_FOUND_ACCOMPANY_STATUS);
    }
}
