package connectripbe.connectrip_be.global.exception.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    /**
     * 400 Bad Request
     */
    // Common error
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    // Member error
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    INVALID_AUTH_CODE(HttpStatus.BAD_REQUEST, "인증 코드가 일치하지 않습니다."),
    EMAIL_NOT_VERIFIED(HttpStatus.BAD_REQUEST, "이메일 인증이 완료되지 않았습니다."),
    WRITE_NOT_YOURSELF(HttpStatus.BAD_REQUEST, "본인이 작성한 글만 수정 또는 삭제할 수 있습니다."),
    COMMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 댓글을 찾을 수 없습니다."),
    PROFILE_IMAGE_UPLOAD_ERROR(HttpStatus.BAD_REQUEST, "프로필 이미지 업로드 중 오류가 발생했습니다."),
    POST_IMAGE_UPLOAD_ERROR(HttpStatus.BAD_REQUEST, "게시물 이미지 업로드 중 오류가 발생했습니다."),
    ALREADY_LIKED(HttpStatus.BAD_REQUEST, "이미 좋아요를 누른 게시물입니다."),
    ALREADY_UNLIKED(HttpStatus.BAD_REQUEST, "이미 좋아요를 취소한 게시물입니다."),
    NICKNAME_DUPLICATION(HttpStatus.BAD_REQUEST, "이미 사용중인 닉네임입니다."),
    PASSWORD_EMPTY(HttpStatus.BAD_REQUEST, "비밀번호를 입력해주세요."),
    OVER_MAX_PARTICIPANTS(HttpStatus.BAD_REQUEST, "모임 최대 인원수를 초과했습니다."),
    PENDING_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 신청을 찾을 수 없습니다."),
    PENDING_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 신청한 상태입니다."),
    WRITE_YOURSELF(HttpStatus.BAD_REQUEST, "본인이 작성한 글은 신청할 수 없습니다."),
    NOT_CHATROOM_LEADER(HttpStatus.BAD_REQUEST, "채팅방 방장이 아닙니다."),
    TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST, "토큰을 찾을 수 없습니다."),

    //Notification type erro
    UNSUPPORTED_POST_TYPE(HttpStatus.BAD_REQUEST, "지원하지 않는 게시물 타입입니다."),

    // Member, 사용자
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    DUPLICATE_MEMBER_NICKNAME(HttpStatus.CONFLICT, "중복된 닉네임입니다."),

    // info-noah: 세분화 시 변경될 수 있음
    MEMBER_NOT_OWNER_EXCEPTION(HttpStatus.FORBIDDEN, "소유자가 아닙니다."),

    // Accompany Post, 동행 게시글
    NOT_FOUND_ACCOMPANY_POST(HttpStatus.NOT_FOUND, "동행 게시글을 찾을 수 없습니다."),
    DUPLICATED_CUSTOM_URL(HttpStatus.CONFLICT, "중복된 커스텀 URL 입니다."),

    //  Accompany Status, 동행 상태
    NOT_FOUND_ACCOMPANY_STATUS(HttpStatus.INTERNAL_SERVER_ERROR, "동행 상태를 찾을 수 없습니다."),
    ALREADY_FINISHED_ACCOMPANY_STATUS_EXCEPTION(HttpStatus.BAD_REQUEST, "이미 종료된 동행압니다."),

    // Pending List, 신청 목록
    PENDING_ALREADY_ACCEPTED(HttpStatus.BAD_REQUEST, "이미 수락된 신청입니다."),
    PENDING_ALREADY_REJECTED(HttpStatus.BAD_REQUEST, "이미 거절된 신청입니다."),

    // ChatRoom error
    ALREADY_JOINED_CHAT_ROOM(HttpStatus.BAD_REQUEST, "이미 참여한 채팅방입니다."),
    ALREADY_EXITED_CHAT_ROOM(HttpStatus.BAD_REQUEST, "이미 나간 채팅방입니다."),
    USER_NOT_IN_CHAT_ROOM(HttpStatus.BAD_REQUEST, "사용자가 채팅방에 참여하지 않았습니다."),
    /**
     * 401 Unauthorized
     */
    // User error
    USER_AUTHORITY_NOT_MATCH(HttpStatus.UNAUTHORIZED, "사용자 권한이 일치하지 않습니다."),

    // Security(jwt) error
    UNKNOWN_ERROR(HttpStatus.UNAUTHORIZED, "알 수 없는 오류가 발생했습니다."),

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),

    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "지원하지 않는 토큰입니다."),

    WRONG_TYPE_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 유형의 토큰입니다."),

    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "액세스 토큰이 만료되었습니다. 재발급이 필요합니다."),

    NOT_FOUND_SESSION(HttpStatus.UNAUTHORIZED, "세션을 찾을 수 없습니다."),

    /**
     * 404 Not Found
     */

    PENDING_LIST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 신청 목록을 찾을 수 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 이메일로 사용자를 찾을 수 없습니다."),
    NOT_FOUND_REVIEW(HttpStatus.NOT_FOUND, "리뷰를 찾을 수 없습니다."),  // 리뷰 관련 에러 추가

    CHAT_ROOM_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 채팅방 참여자를 찾을 수 없습니다."),
    CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 채팅방을 찾을 수 없습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 글을 찾을 수 없습니다."),
    CHATROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 채팅방을 찾을 수 없습니다."),
    CHAT_MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 채팅 메세지를 찾을 수 없습니다."),
    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 알림을 찾을 수 없습니다."),

    /**
     * 409 Conflict
     */

    // User error
    DUPLICATE_USER(HttpStatus.CONFLICT, "이미 존재하는 사용자입니다."),
    REVIEW_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 동일한 리뷰가 존재합니다."),  // 리뷰 관련 에러 추가

    /**
     * 500 Internal Server Error
     */
    REDIS_CAST_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Redis 데이터 변환 중 오류가 발생했습니다."),

    REDIS_GET_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Redis 데이터 조회 중 오류가 발생했습니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류가 발생했습니다."),

    REDIRECT_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "리다이렉트에 실패했습니다."),

    API_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "외부 API 서버 오류가 발생했습니다."),

    /**
     * 403 Forbidden
     */
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근이 거부되었습니다."),
    REVIEW_NOT_ALLOWED(HttpStatus.FORBIDDEN, "리뷰를 작성할 수 있는 상태가 아닙니다."), // 리뷰 관련 에러 추가
    REVIEW_DELETE_NOT_ALLOWED(HttpStatus.FORBIDDEN, "리뷰를 삭제할 권한이 없습니다."),  // 리뷰 관련 에러 추가
    UNAUTHORIZED(HttpStatus.FORBIDDEN, "권한이 없습니다."),

    /**
     * 405 Method Not Allowed
     */
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 HTTP 메서드입니다."),

    /**
     * 429 Too Many Requests
     */

    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "요청이 너무 많습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String description;
}
