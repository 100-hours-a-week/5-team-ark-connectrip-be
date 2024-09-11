package connectripbe.connectrip_be.communitypost.repository;

import connectripbe.connectrip_be.communitypost.entity.CommunityPostEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityPostRepository extends JpaRepository<CommunityPostEntity, Long> {

    // ID를 기준으로 삭제되 않은 게시글 조회
    Optional<CommunityPostEntity> findByIdAndDeletedAtIsNull(Long id);

    // 삭제되지 않은 모든 게시글 조회
    List<CommunityPostEntity> findAllByDeletedAtIsNullOrderByCreatedAtDesc();

    @Query("SELECT cp FROM CommunityPostEntity cp WHERE (cp.title LIKE %:query% OR cp.content LIKE %:query%) AND cp.deletedAt IS NULL ORDER BY cp.createdAt DESC")
    List<CommunityPostEntity> findAllByQuery(@Param("query") String query);
}