package connectripbe.connectrip_be.global.config;
import connectripbe.connectrip_be.auth.config.LoginUserArgumentResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
      private final LoginUserArgumentResolver loginUserArgumentResolver;
      @Override
      public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
            // loginUserArgumentResolver 를 Argument Resolver 목록에 추가
            resolvers.add(loginUserArgumentResolver);
      }

      @Override
      public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOrigins("https://main--beo-gather.netlify.app", "http://localhost:5173", "http://localhost:3000")  // 여러 출처를 허용
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")  // 허용할 HTTP 메소드 지정
                    .allowedHeaders("*")  // 모든 헤더 허용
                    .allowCredentials(true)  // 쿠키를 포함한 요청 허용
                    .maxAge(3600);  // preflight 리퀘스트를 캐싱할 시간 지정
      }

}
