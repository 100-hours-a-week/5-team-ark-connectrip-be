//package connectripbe.connectrip_be.global.config;
//
//import io.sentry.Sentry;
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//
//@Profile("prod")
//@Configuration
//public class SentryConfig {
//
////    @Value("${sentry.dsn}")
////    private String sentryDsn;
//
//    @PostConstruct
//    public void init() {
//        Sentry.init(options -> options.setDsn("https://e1612dab26d1f71e0679a75d65ff692a@o4507838250418176.ingest.us.sentry.io/4507838415044608"));
//    }
//}
