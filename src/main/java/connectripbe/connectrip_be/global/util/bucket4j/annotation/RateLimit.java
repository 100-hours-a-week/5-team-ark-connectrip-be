package connectripbe.connectrip_be.global.util.bucket4j.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RateLimit {
    int capacity() default 5;  // 기본 용량

    int refillTokens() default 5;  // 재충전되는 토큰 수

    long refillDurationSeconds() default 60;  // 재충전 주기(초 단위)
}
