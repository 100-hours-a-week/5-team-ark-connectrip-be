package connectripbe.connectrip_be.member.exception;

import connectripbe.connectrip_be.global.exception.GlobalException;
import connectripbe.connectrip_be.global.exception.type.ErrorCode;

public class MemberNotOwnerException extends GlobalException {

    public MemberNotOwnerException() {
        super(ErrorCode.MEMBER_NOT_OWNER_EXCEPTION);
    }
}
