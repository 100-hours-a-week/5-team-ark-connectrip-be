package connectripbe.connectrip_be.accompany.post.exception;

import connectripbe.connectrip_be.global.exception.GlobalException;
import connectripbe.connectrip_be.global.exception.type.ErrorCode;

public class NotFoundAccompanyPostException extends GlobalException {

    public NotFoundAccompanyPostException() {
        super(ErrorCode.NOT_FOUND_ACCOMPANY_POST);
    }
}
