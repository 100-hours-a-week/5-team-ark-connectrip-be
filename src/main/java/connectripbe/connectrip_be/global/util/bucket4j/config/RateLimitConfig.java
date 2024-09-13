package connectripbe.connectrip_be.global.util.bucket4j.config;


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

    private final RedisClient lettuceRedisClient;

    /**
     * Redis 에 연결된 Lettuce 기반의 Bucket4j ProxyManager 를 생성.
     *
     * @return Lettuce 기반의 ProxyManager 인스턴스
     */
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
}
