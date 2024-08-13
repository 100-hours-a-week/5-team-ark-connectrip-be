package connectripbe.connectrip_be.auth.config;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;


import connectripbe.connectrip_be.auth.jwt.JwtAccessDeniedHandler;
import connectripbe.connectrip_be.auth.jwt.JwtAuthenticationEntryPoint;
import connectripbe.connectrip_be.auth.jwt.JwtAuthenticationFilter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

      private final JwtAuthenticationFilter jwtAuthenticationFilter;
      private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
      private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

      @Bean
      public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
      }

      @Bean
      public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
            httpSecurity
                    .csrf(AbstractHttpConfigurer::disable)
                    .formLogin(AbstractHttpConfigurer::disable)
                    .httpBasic(AbstractHttpConfigurer::disable)
                    .cors(withDefaults())
                    .sessionManagement((sessionManagement) ->
                            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    )
                    .authorizeHttpRequests(
                            auth -> auth
                                    .requestMatchers(requestHasRoleUser()).hasRole("USER")
                                    .requestMatchers(requestHasAnyRoleUserAdmin()).hasRole("ADMIN")
                                    .requestMatchers(requestHasAnyRoleUserAdmin())
                                    .hasAnyRole("USER", "ADMIN")
                                    .anyRequest().permitAll()
                    ).exceptionHandling(configurer -> {
                          configurer.authenticationEntryPoint(jwtAuthenticationEntryPoint);
                          configurer.accessDeniedHandler(jwtAccessDeniedHandler);
                    })
                    .addFilterBefore(jwtAuthenticationFilter,
                            UsernamePasswordAuthenticationFilter.class);

            return httpSecurity.build();
      }

      private RequestMatcher[] requestHasRoleUser() {
            List<RequestMatcher> requestMatchers = List.of(
                    antMatcher("/api/v1/member"),
                    antMatcher(POST, "/api/v1/post"),
                    antMatcher(PATCH, "/api/v1/post"),
                    antMatcher(DELETE, "/api/v1/post"),
                    antMatcher(GET, "/api/v1/post"),
                    antMatcher(POST,"/api/v1/comment"),
                    antMatcher(PATCH,"/api/v1/comment"),
                    antMatcher(DELETE,"/api/v1/comment"),
                    antMatcher(GET,"/api/v1/comment")

            );
            return requestMatchers.toArray(RequestMatcher[]::new);
      }

      private RequestMatcher[] requestHasRoleAdmin() {

            List<RequestMatcher> requestMatchers = List.of(
                    antMatcher(DELETE,"/api/comment") // 보류
            );
            return requestMatchers.toArray(RequestMatcher[]::new);
      }

      private RequestMatcher[] requestHasAnyRoleUserAdmin() {

            List<RequestMatcher> requestMatchers = List.of(
                    antMatcher("/api/comment") // 보류
            );
            return requestMatchers.toArray(RequestMatcher[]::new);
      }
}

