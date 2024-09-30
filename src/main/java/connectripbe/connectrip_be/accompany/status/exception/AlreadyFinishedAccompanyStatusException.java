package connectripbe.connectrip_be.accompany.status.exception;

import connectripbe.connectrip_be.global.exception.GlobalException;
import connectripbe.connectrip_be.global.exception.type.ErrorCode;

public class AlreadyFinishedAccompanyStatusException extends GlobalException {

    public AlreadyFinishedAccompanyStatusException() {
        super(ErrorCode.ALREADY_FINISHED_ACCOMPANY_STATUS_EXCEPTION);
    }
}
