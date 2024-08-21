package connectripbe.connectrip_be.post.repository;

import connectripbe.connectrip_be.post.entity.AccompanyPostEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository 
public interface AccompanyPostRepository extends JpaRepository<AccompanyPostEntity, Long> {

    @Query("SELECT ap FROM AccompanyPostEntity ap WHERE ap.title LIKE %:query% OR ap.content LIKE %:query% ORDER BY ap.createdAt DESC")
    List<AccompanyPostEntity> findAllByTitleOrContentContainingOrderByCreatedAtDesc(@Param("query") String query);
    
    List<AccompanyPostEntity> findAllByOrderByCreatedAtDesc();

}
