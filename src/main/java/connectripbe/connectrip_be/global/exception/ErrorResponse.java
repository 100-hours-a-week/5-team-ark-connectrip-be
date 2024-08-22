package connectripbe.connectrip_be.global.exception;


import connectripbe.connectrip_be.global.exception.type.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ErrorResponse {

    private ErrorCode errorCode;
    private HttpStatus httpStatus;
    private String errorMessage;

}
