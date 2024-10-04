package connectripbe.connectrip_be.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Example API Docs",
                description = "Description",
                version = "v1"
        )
)
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        String securityJwtName = "JWT";

        // 쿠키 기반 보안 요구 사항 설정
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(securityJwtName);

        // 쿠키 기반 SecurityScheme 설정
        Components components = new Components()
                .addSecuritySchemes(securityJwtName, new SecurityScheme()
                        .name(securityJwtName)
                        .type(SecurityScheme.Type.APIKEY)
                        .in(SecurityScheme.In.COOKIE) // 쿠키를 통해 인증
                        .bearerFormat(securityJwtName) // JWT 형식
                        .scheme("cookie"));

        // OpenAPI 반환: 보안 요구 사항 및 보안 스키마를 포함
        return new OpenAPI()
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}

