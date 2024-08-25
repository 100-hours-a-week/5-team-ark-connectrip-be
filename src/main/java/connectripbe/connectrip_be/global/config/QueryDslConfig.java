package connectripbe.connectrip_be.global.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// QueryDslConfig 클래스는 QueryDSL을 사용하기 위한 설정 클래스.
@Configuration
public class QueryDslConfig {

    @PersistenceContext
    private EntityManager entityManager;

    // JPAQueryFactory 빈을 등록
    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
