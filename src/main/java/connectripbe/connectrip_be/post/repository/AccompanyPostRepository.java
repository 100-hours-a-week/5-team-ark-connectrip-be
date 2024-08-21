package connectripbe.connectrip_be.post.repository;

import connectripbe.connectrip_be.post.entity.AccompanyPostEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccompanyPostRepository extends JpaRepository<AccompanyPostEntity, Long> {

      List<AccompanyPostEntity> findAllByOrderByCreatedAtDesc();

}
