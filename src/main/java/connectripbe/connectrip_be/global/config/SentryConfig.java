package connectripbe.connectrip_be.global.config;

import io.sentry.Sentry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("prod")
@Configuration
public class SentryConfig {

    @Value("${sentry.dsn}")
    private String sentryDsn;

    // @EnableSentry(dsn = "") 설정
    public SentryConfig() {
        Sentry.init(options -> options.setDsn(sentryDsn));
    }
}
