package connectripbe.connectrip_be.global.util.bucket4j.config;


import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Slf4j
@RequiredArgsConstructor
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Configuration
public class RateLimitConfig {

    // 버킷에 담길 수 있는 토큰의 최대 수
    private static final int CAPACITY = 5;

    // REFILL_DURATION 으로 지정된 시간 동안 버킷에 추가될 토큰의 수
    private static final int REFILL_TOKEN_AMOUNT = 5;

    // 토큰이 재충전되는 빈도
    private static final Duration REFILL_DURATION = Duration.ofSeconds(60);

    private final RedisClient lettuceRedisClient;

    @Bean
    public LettuceBasedProxyManager<String> lettuceBasedProxyManager() {
        StatefulRedisConnection<String, byte[]> connect =
                lettuceRedisClient.connect(RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE));

        return LettuceBasedProxyManager.builderFor(connect)
                // 버킷 만료 정책(최대 용량으로 채워지는데 필요한 60초 동안 버킷으로 유지)
                .withExpirationStrategy(
                        ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(Duration.ofSeconds(60)))
                .build();
    }

    @Bean
    public BucketConfiguration bucketConfiguration() {
        return BucketConfiguration.builder()
                // 새로운 방식으로 토큰 충전: 일정한 시간 간격으로 충전(그리디 방식)
                .addLimit(Bandwidth.builder()
                        .capacity(CAPACITY)
                        .refillGreedy(REFILL_TOKEN_AMOUNT, REFILL_DURATION)
                        .build())
                .build();
    }
}
