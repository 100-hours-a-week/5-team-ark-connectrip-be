package connectripbe.connectrip_be.comment.repository;

import connectripbe.connectrip_be.comment.entity.AccompanyCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccompanyCommentRepository extends JpaRepository<AccompanyCommentEntity, Long> {
    // AccompanyPostEntity의 ID로 삭제되지 않은 댓글 목록 조회
    List<AccompanyCommentEntity> findByAccompanyPostEntity_IdAndDeletedAtIsNull(Long postId);
}
