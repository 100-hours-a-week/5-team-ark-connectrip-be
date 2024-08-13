package connectripbe.connectrip_be.auth.jwt;

import static connectripbe.connectrip_be.global.exception.type.ErrorCode.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 * JWT 인증 시, 접근 권한이 없는 사용자가 보호된 리소스에 대한 권한 없음 처리
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

      //ObjectMapper 는 JSON 변환을 담당
      private final ObjectMapper objectMapper;

      /**
       * 권한이 없는 사용자가 보호된 리소스에 접근을 시도했을 때의 처리 메소드
       */
      @Override
      public void handle(HttpServletRequest request, HttpServletResponse response,
              AccessDeniedException accessDeniedException) throws IOException, ServletException {

            // 권한 없음 오류에 대한 응답을 설정
            setResponse(response);

      }

      /**
       * 권한 없음 오류에 대한 응답을 생성하고, 이를 클라이언트에 전송하는 메소드
       * 한글 출력을 위해 getWriter() 사용
       */
      private void setResponse(HttpServletResponse response) throws IOException {

            Map<String, Object> map = new HashMap<>();

            // 오류 코드와 메시지를 응답에 포함
            map.put("errorCode", USER_AUTHORITY_NOT_MATCH);
            map.put("errorMessage", USER_AUTHORITY_NOT_MATCH.getDescription());

            // 응답의 컨텐츠 타입과 상태 코드를 설정
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(USER_AUTHORITY_NOT_MATCH.getHttpStatus().value());

            log.error("JWT token error -> {}", USER_AUTHORITY_NOT_MATCH);

            // 응답에 JSON 형태로 변환하여 클라이언트에 전송
            response.getWriter().println(objectMapper.writeValueAsString(map));

      }
}
