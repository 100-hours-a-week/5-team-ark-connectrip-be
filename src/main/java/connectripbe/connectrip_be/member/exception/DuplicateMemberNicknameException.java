package connectripbe.connectrip_be.member.exception;

import connectripbe.connectrip_be.global.exception.GlobalException;
import connectripbe.connectrip_be.global.exception.type.ErrorCode;

public class DuplicateMemberNicknameException extends GlobalException {

    public DuplicateMemberNicknameException() {
        super(ErrorCode.DUPLICATE_MEMBER_NICKNAME);
    }
}
