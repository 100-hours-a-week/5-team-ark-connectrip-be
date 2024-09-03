package connectripbe.connectrip_be.communitypost.repository;

import connectripbe.connectrip_be.communitypost.entity.CommunityPostEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityPostRepository extends JpaRepository<CommunityPostEntity, Long> {

    // ID를 기준으로 삭제되 않은 게시글 조회
    Optional<CommunityPostEntity> findByIdAndDeletedAtIsNull(Long id);

    // 삭제되지 않은 모든 게시글 조회
    List<CommunityPostEntity> findAllByDeletedAtIsNullOrderByCreatedAtDesc();
}
