package connectripbe.connectrip_be.auth.exception;

import connectripbe.connectrip_be.global.exception.GlobalException;
import connectripbe.connectrip_be.global.exception.type.ErrorCode;

public class RedirectFailureException extends GlobalException {

    public RedirectFailureException() {
        super(ErrorCode.REDIRECT_FAILURE);
    }
}
