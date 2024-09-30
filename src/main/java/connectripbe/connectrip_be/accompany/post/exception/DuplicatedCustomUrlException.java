package connectripbe.connectrip_be.accompany.post.exception;

import connectripbe.connectrip_be.global.exception.GlobalException;
import connectripbe.connectrip_be.global.exception.type.ErrorCode;

public class DuplicatedCustomUrlException extends GlobalException {

    public DuplicatedCustomUrlException() {
        super(ErrorCode.DUPLICATED_CUSTOM_URL);
    }
}
