package connectripbe.connectrip_be.global.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

      @ExceptionHandler(GlobalException.class)
      public ErrorResponse handleCustomException(GlobalException e){
            log.error("{} is occurred.", e.getErrorCode());

        return new ErrorResponse(e.getErrorCode(),
            e.getErrorCode().getHttpStatus(), e.getErrorCode().getDescription());
      }

}
