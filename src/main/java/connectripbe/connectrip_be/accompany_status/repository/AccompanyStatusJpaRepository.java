package connectripbe.connectrip_be.accompany_status.repository;

import connectripbe.connectrip_be.accompany_status.entity.AccompanyStatusEntity;
import connectripbe.connectrip_be.post.entity.AccompanyPostEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccompanyStatusJpaRepository extends JpaRepository<AccompanyStatusEntity, Long> {

    Optional<AccompanyStatusEntity> findTopByAccompanyPostEntityOrderByCreatedAtDesc(
            AccompanyPostEntity accompanyPostEntity);

}
