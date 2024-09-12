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

    private Bucket getOrCreateBucket(String bucketKey, int capacity, int refillTokens, long refillDurationSeconds) {
        BucketConfiguration configuration = BucketConfiguration.builder()
                .addLimit(Bandwidth.builder()
                        .capacity(capacity)
                        .refillGreedy(refillTokens, Duration.ofSeconds(refillDurationSeconds))
                        .build())
                .build();

        return rateLimitConfig.lettuceBasedProxyManager()
                .builder()
                .build(bucketKey, () -> configuration);
    }

    private ConsumptionProbe consumeToken(Bucket bucket) {
        return bucket.tryConsumeAndReturnRemaining(1);
    }

    private void logConsumption(String bucketKey, ConsumptionProbe probe) {
        log.info("Bucket Key: {}, tryConsume: {}, remainToken: {}, tryTime: {}",
                bucketKey, probe.isConsumed(), probe.getRemainingTokens(), LocalDateTime.now());
    }

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
