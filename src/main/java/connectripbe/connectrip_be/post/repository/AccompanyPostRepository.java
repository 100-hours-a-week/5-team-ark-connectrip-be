package connectripbe.connectrip_be.post.repository;

import connectripbe.connectrip_be.post.entity.AccompanyPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccompanyPostRepository extends JpaRepository<AccompanyPostEntity, Long> {

    @Query("SELECT ap FROM AccompanyPostEntity ap WHERE ap.title LIKE %:query% OR ap.content LIKE %:query% ORDER BY ap.createdAt DESC")
    List<AccompanyPostEntity> findAllByTitleContainingOrContentContainingOrderByCreatedAtDesc(@Param("query") String query);
}
