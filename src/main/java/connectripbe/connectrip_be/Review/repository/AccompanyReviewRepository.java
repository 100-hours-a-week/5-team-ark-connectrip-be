package connectripbe.connectrip_be.Review.repository;

import connectripbe.connectrip_be.Review.entity.AccompanyReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccompanyReviewRepository extends JpaRepository<AccompanyReviewEntity, Long> {
    List<AccompanyReviewEntity> findByChatRoomId(Long chatRoomId);  // 수정된 부분
}
