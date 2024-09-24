package connectripbe.connectrip_be.post.repository;

import connectripbe.connectrip_be.post.entity.AccompanyPostEntity;
import java.util.List;
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

    @Query("SELECT ap FROM AccompanyPostEntity ap WHERE (ap.title LIKE %:query% OR ap.content LIKE %:query%) AND ap.deletedAt IS NULL ORDER BY ap.createdAt DESC")
    List<AccompanyPostEntity> findAllByQuery(@Param("query") String query);

    Page<AccompanyPostEntity> findAllByDeletedAtIsNull(Pageable pageable);

    boolean existsByCustomUrl(String customUrl);
}
