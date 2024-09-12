package connectripbe.connectrip_be.global.util.bucket4j.aop;

import connectripbe.connectrip_be.global.exception.GlobalException;
import connectripbe.connectrip_be.global.exception.type.ErrorCode;
import connectripbe.connectrip_be.global.util.bucket4j.annotation.RateLimit;
import connectripbe.connectrip_be.global.util.bucket4j.service.RateLimiterService;
import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
@Slf4j
@Aspect
public class RateLimitAspect {

    private final RateLimiterService rateLimiterService;

    @Around("@annotation(connectripbe.connectrip_be.global.util.bucket4j.annotation.RateLimit)")
    public Object checkRateLimit(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        RateLimit rateLimit = method.getAnnotation(RateLimit.class);

        int capacity = rateLimit.capacity();
        int refillTokens = rateLimit.refillTokens();
        long refillDurationSeconds = rateLimit.refillDurationSeconds();

        // SecurityContext에서 사용자 ID를 가져옴
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        // API 이름 또는 경로를 사용하여 버킷을 구분
        String apiEndpoint = method.getName(); // 여기서 method.getName()을 사용해 메서드 이름을 API 경로로 활용

        log.info("Authenticated user ID: {}, API: {}", userId, apiEndpoint);

        if (!rateLimiterService.tryConsume(userId, apiEndpoint, capacity, refillTokens, refillDurationSeconds)) {
            throw new GlobalException(ErrorCode.TOO_MANY_REQUESTS);
        }

        return joinPoint.proceed();
    }

}
