package connectripbe.connectrip_be.global.util.bucket4j.service;

import connectripbe.connectrip_be.global.exception.GlobalException;
import connectripbe.connectrip_be.global.exception.type.ErrorCode;
import connectripbe.connectrip_be.global.util.bucket4j.config.RateLimitConfig;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.ConsumptionProbe;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RateLimiterService {

    private final RateLimitConfig rateLimitConfig;

    /**
     * 주어진 사용자 ID와 API 엔드포인트에 대해 레이트 리미트를 적용한 토큰 소비 시도를 수행.
     *
     * @param userId                사용자 ID
     * @param apiEndpoint           API 엔드포인트 이름 또는 경로
     * @param capacity              버킷의 최대 용량
     * @param refillTokens          버킷이 충전되는 토큰 수
     * @param refillDurationSeconds 토큰 충전 주기 (초 단위)
     * @return 토큰이 소비되었으면 true, 그렇지 않으면 false
     */
    public boolean tryConsume(String userId, String apiEndpoint, int capacity, int refillTokens,
                              long refillDurationSeconds) {
        // userId와 api Endpoint로 고유한 키 생성
        String bucketKey = userId + ":" + apiEndpoint;

        Bucket bucket = getOrCreateBucket(bucketKey, capacity, refillTokens, refillDurationSeconds);

        ConsumptionProbe probe = consumeToken(bucket);

        logConsumption(bucketKey, probe);

        handleNotConsumed(probe);

        return probe.isConsumed();
    }

    /**
     * 주어진 버킷 키와 설정에 따라 버킷을 생성하거나 기존 버킷을 반환.
     *
     * @param bucketKey             고유한 버킷 키
     * @param capacity              버킷의 최대 용량
     * @param refillTokens          버킷이 충전되는 토큰 수
     * @param refillDurationSeconds 토큰 충전 주기 (초 단위)
     * @return 설정된 버킷 객체
     */
    private Bucket getOrCreateBucket(String bucketKey, int capacity, int refillTokens, long refillDurationSeconds) {
        BucketConfiguration configuration = BucketConfiguration.builder()
                .addLimit(Bandwidth.builder()
                        .capacity(capacity)
                        .refillIntervally(refillTokens, Duration.ofSeconds(refillDurationSeconds))
                        .build())
                .build();

        return rateLimitConfig.lettuceBasedProxyManager()
                .builder()
                .build(bucketKey, () -> configuration);
    }

    /**
     * 버킷에서 토큰을 소비하고 결과를 반환.
     *
     * @param bucket 소비할 버킷 객체
     * @return 소비된 토큰의 결과
     */
    private ConsumptionProbe consumeToken(Bucket bucket) {
        return bucket.tryConsumeAndReturnRemaining(1);
    }


    /**
     * 소비된 토큰에 대한 정보를 로그로 기록.
     *
     * @param bucketKey 버킷 키
     * @param probe     토큰 소비 결과 객체
     */
    private void logConsumption(String bucketKey, ConsumptionProbe probe) {
        log.info("Bucket Key: {}, tryConsume: {}, remainToken: {}, tryTime: {}",
                bucketKey, probe.isConsumed(), probe.getRemainingTokens(), LocalDateTime.now());
    }


    /**
     * 토큰이 소비되지 못했을 때 예외를 발생.
     *
     * @param probe 토큰 소비 결과 객체
     * @throws GlobalException 요청이 너무 많은 경우 예외를 발생
     */
    private void handleNotConsumed(ConsumptionProbe probe) {
        if (!probe.isConsumed()) {
            throw new GlobalException(ErrorCode.TOO_MANY_REQUESTS);
        }
    }

    public long getRemainToken(String bucketKey) {
        Bucket bucket = getOrCreateBucket(bucketKey, 5, 5, 60);
        return bucket.getAvailableTokens();
    }

}
