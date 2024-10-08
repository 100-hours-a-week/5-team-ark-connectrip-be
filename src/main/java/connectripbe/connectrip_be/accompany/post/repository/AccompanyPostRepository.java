package connectripbe.connectrip_be.accompany.post.repository;

import connectripbe.connectrip_be.accompany.post.entity.AccompanyPostEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccompanyPostRepository extends JpaRepository<AccompanyPostEntity, Long> {

    Optional<AccompanyPostEntity> findByCustomUrl(String customUrl);

    Optional<AccompanyPostEntity> findByIdAndDeletedAtIsNull(long id);

    @Query("SELECT ap FROM AccompanyPostEntity ap WHERE (ap.title LIKE %:query% OR ap.content LIKE %:query%) AND ap.deletedAt IS NULL")
    Page<AccompanyPostEntity> findAllByQueryAndDeletedAtIsNull(@Param("query") String query, Pageable pageable);

    Page<AccompanyPostEntity> findAllByDeletedAtIsNull(Pageable pageable);

    boolean existsByCustomUrl(String customUrl);
}
