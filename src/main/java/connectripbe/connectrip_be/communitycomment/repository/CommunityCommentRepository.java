package connectripbe.connectrip_be.communitycomment.repository;

import connectripbe.connectrip_be.communitycomment.entity.CommunityCommentEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityCommentRepository extends JpaRepository<CommunityCommentEntity, Long> {

    Optional<CommunityCommentEntity> findByIdAndDeletedAtIsNull(long id);

    // CommunityPostEntity 의 ID로 삭제되지 않은 댓글 목록 조회
    List<CommunityCommentEntity> findByCommunityPostEntity_IdAndDeletedAtIsNull(Long postId);
}
