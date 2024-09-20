package connectripbe.connectrip_be.commentnotification.repository;

import connectripbe.connectrip_be.commentnotification.entity.CommentNotificationEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentNotificationRepository extends JpaRepository<CommentNotificationEntity, Long> {
    List<CommentNotificationEntity> findByMemberIdAndReadAtIsNull(Long memberId);
}

