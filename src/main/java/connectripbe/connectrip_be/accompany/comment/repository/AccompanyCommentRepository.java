package connectripbe.connectrip_be.accompany.comment.repository;

import connectripbe.connectrip_be.accompany.comment.entity.AccompanyCommentEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccompanyCommentRepository extends JpaRepository<AccompanyCommentEntity, Long> {

    Optional<AccompanyCommentEntity> findByIdAndDeletedAtIsNull(long id);

    // AccompanyPostEntity 의 ID로 삭제되지 않은 댓글 목록 조회
    List<AccompanyCommentEntity> findByAccompanyPostEntity_IdAndDeletedAtIsNull(Long postId);
}
