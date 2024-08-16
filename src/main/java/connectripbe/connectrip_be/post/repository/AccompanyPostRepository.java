package connectripbe.connectrip_be.post.repository;

import connectripbe.connectrip_be.post.entity.AccompanyPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // 이 인터페이스가 스프링의 리포지토리임을 나타냄
public interface AccompanyPostRepository extends JpaRepository<AccompanyPost, Long> {

}
