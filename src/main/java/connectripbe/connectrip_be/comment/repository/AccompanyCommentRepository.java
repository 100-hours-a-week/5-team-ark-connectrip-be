package connectripbe.connectrip_be.comment.repository;

import connectripbe.connectrip_be.comment.entity.AccompanyCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccompanyCommentRepository extends JpaRepository<AccompanyCommentEntity, Long> {
    // JpaRepository가 기본 CRUD 메서드를 제공하므로, 별도의 구현 필요 없음
}
