package connectripbe.connectrip_be.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jackson 의 ObjectMapper 를 커스터 마이징
 * Java 8 날짜 및 시간을 처리하여 사람이 읽을 수 있는 형식으로 직렬화
 */
@Configuration
public class AppConfig {

      /**
       *  ObjectMapper 를 커스터마이징하여 Spring 컨텍스트에 빈으로 등록하는 메서드
       * @return 커스터 마이징 된 ObjectMapper
       */
      @Bean
      public ObjectMapper objectMapper(){
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            return objectMapper;
      }
}
