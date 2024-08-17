package connectripbe.connectrip_be.comment.repository;

import connectripbe.connectrip_be.comment.entity.AccompanyComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccompanyCommentRepository extends JpaRepository<AccompanyComment, Long> {
    // JpaRepository가 기본 CRUD 메서드를 제공하므로, 별도의 구현 필요 없음
}
