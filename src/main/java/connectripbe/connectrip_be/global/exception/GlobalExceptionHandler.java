package connectripbe.connectrip_be.global.exception;


import static connectripbe.connectrip_be.global.exception.type.ErrorCode.INTERNAL_SERVER_ERROR;

import com.amazonaws.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(GlobalException e) {
        log.error("{} is occurred.", e.getErrorCode());

        return new ResponseEntity<>(
                new ErrorResponse(e.getErrorCode(), e.getErrorCode().getHttpStatus(),
                        e.getErrorCode().getDescription()),
                e.getErrorCode().getHttpStatus()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(Exception e) {
        log.error("Exception is occurred", e);
        return new ResponseEntity<>(
                new ErrorResponse(INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR,
                        INTERNAL_SERVER_ERROR.getDescription()).getHttpStatus());
    }

}
