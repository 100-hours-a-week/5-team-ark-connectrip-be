package connectripbe.connectrip_be.community.post.repository;

import connectripbe.connectrip_be.community.post.entity.CommunityPostEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityPostRepository extends JpaRepository<CommunityPostEntity, Long> {

    // ID를 기준으로 삭제되 않은 게시글 조회
    Optional<CommunityPostEntity> findByIdAndDeletedAtIsNull(Long id);

    Page<CommunityPostEntity> findAllByDeletedAtIsNull(Pageable pageable);

    @Query("SELECT cp FROM CommunityPostEntity cp WHERE (cp.title LIKE %:query% OR cp.content LIKE %:query%) AND cp.deletedAt IS NULL")
    Page<CommunityPostEntity> findAllByQueryAndDeletedAtIsNull(@Param("query") String query, Pageable pageable);
}
