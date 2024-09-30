package connectripbe.connectrip_be.accompany.status.repository;

import connectripbe.connectrip_be.accompany.post.entity.AccompanyPostEntity;
import connectripbe.connectrip_be.accompany.status.entity.AccompanyStatusEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccompanyStatusJpaRepository extends JpaRepository<AccompanyStatusEntity, Long> {

    Optional<AccompanyStatusEntity> findTopByAccompanyPostEntityOrderByCreatedAtDesc(
            AccompanyPostEntity accompanyPostEntity);

}
