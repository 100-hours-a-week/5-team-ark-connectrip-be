package connectripbe.connectrip_be.accompany_status.repository;

import connectripbe.connectrip_be.accompany_status.entity.AccompanyStatusEntity;
import connectripbe.connectrip_be.post.entity.AccompanyPostEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccompanyStatusJpaRepository extends JpaRepository<AccompanyStatusEntity, Long> {

    Optional<AccompanyStatusEntity> findTopByAccompanyPostEntityOrderByCreatedAtDesc(
            AccompanyPostEntity accompanyPostEntity);

    @Query("SELECT a FROM AccompanyStatusEntity a WHERE a.accompanyPostEntity = :accompanyPostEntity ORDER BY a.createdAt DESC")
    Optional<AccompanyStatusEntity> findLatestStatusByPost(
            @Param("accompanyPostEntity") AccompanyPostEntity accompanyPostEntity);
}
