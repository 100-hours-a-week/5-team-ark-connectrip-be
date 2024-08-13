package connectripbe.connectrip_be.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;

import connectripbe.connectrip_be.global.exception.ErrorResponse;
import connectripbe.connectrip_be.global.exception.type.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


/**
 * 인증되지 않은 사용자가 보호된 리소스에 접근하려고 할 때 처리
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

      // ObjectMapper 는 JSON 변환을 담당
      private final ObjectMapper objectMapper;

      @Override
      public void commence(HttpServletRequest request, HttpServletResponse response,
              AuthenticationException authException) throws IOException, ServletException {

            Object exception = request.getAttribute("exception");

            // 인증 오류가 ErrorCode 형태인 경우에만 에러 응답을 생성
            if (exception instanceof ErrorCode errorCode) {
                  setResponse(response, errorCode);
            }
      }

      /**
       * 인증 오류에 대한 응답을 생성하고, 이를 클라이언트에 전송하는 메소드.
       * 한글 출력을 위해 getWriter() 사용
       */
      private void setResponse(HttpServletResponse response, ErrorCode errorCode)
              throws IOException {

            Map<String, Object> map = new HashMap<>();

            ErrorResponse errorResponse = new ErrorResponse(errorCode, errorCode.getHttpStatus(),
                    errorCode.getDescription());

            // 오류 코드와 메시지를 응답에 포함
            map.put("errorCode", errorResponse.getErrorCode().name());
            map.put("errorMessage", errorResponse.getErrorCode().getDescription());

            // 응답의 컨텐츠 타입과 상태 코드를 설정
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(errorResponse.getHttpStatus().value());

            log.error("JWT token is error -> {}", errorCode);

            // 응답을 JSON 형태로 변환하여 클라이언트에 전송
            response.getWriter().println(objectMapper.writeValueAsString(map));

      }


}
