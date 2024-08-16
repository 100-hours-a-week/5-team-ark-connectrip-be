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
      POST_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 글을 찾을 수 없습니다."),
      COMMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 댓글을 찾을 수 없습니다."),
      PROFILE_IMAGE_UPLOAD_ERROR(HttpStatus.BAD_REQUEST, "프로필 이미지 업로드 중 오류가 발생했습니다."),
      POST_IMAGE_UPLOAD_ERROR(HttpStatus.BAD_REQUEST, "게시물 이미지 업로드 중 오류가 발생했습니다."),
      ALREADY_LIKED(HttpStatus.BAD_REQUEST, "이미 좋아요를 누른 게시물입니다."),
      ALREADY_UNLIKED(HttpStatus.BAD_REQUEST, "이미 좋아요를 취소한 게시물입니다."),
      NICKNAME_DUPLICATION(HttpStatus.BAD_REQUEST, "이미 사용중인 닉네임입니다."),
      PASSWORD_EMPTY(HttpStatus.BAD_REQUEST, "비밀번호를 입력해주세요."),
      OVER_MAX_PARTICIPANTS(HttpStatus.BAD_REQUEST, "모임 최대 인원수를 초과했습니다."),

      // Accompany Post, 동행 게시글
      NOT_FOUND_ACCOMPANY_POST(HttpStatus.NOT_FOUND, "동행 게시글을 찾을 수 없습니다."),

      // Meeting error
      DUPLICATE_MEETING(HttpStatus.BAD_REQUEST, "이미 모임에 참여하셨습니다."),
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

      /**
       * 404 Not Found
       */

      USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 이메일로 사용자를 찾을 수 없습니다."),

      /**
       * 409 Conflict
       */

      // User error
      DUPLICATE_USER(HttpStatus.CONFLICT, "이미 존재하는 사용자입니다."),

      /**
       * 500 Internal Server Error
       */
      INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류가 발생했습니다."),
      API_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "외부 API 서버 오류가 발생했습니다."),

      /**
       * 403 Forbidden
       */
      ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근이 거부되었습니다."),

      /**
       * 405 Method Not Allowed
       */
      METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 HTTP 메서드입니다."),
      ;

      private final HttpStatus httpStatus;
      private final String description;
}
