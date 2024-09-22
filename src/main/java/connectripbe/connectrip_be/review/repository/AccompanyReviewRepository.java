package connectripbe.connectrip_be.review.repository;

import connectripbe.connectrip_be.review.entity.AccompanyReviewEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccompanyReviewRepository extends JpaRepository<AccompanyReviewEntity, Long> {

    // 특정 채팅방에 대한 리뷰 가져오기
    List<AccompanyReviewEntity> findByChatRoomId(Long chatRoomId);

    // 리뷰어, 대상자, 채팅방을 기준으로 리뷰 존재 여부 확인
    boolean existsByReviewerIdAndTargetIdAndChatRoomId(Long reviewerId, Long targetId, Long chatRoomId);

    // 네이티브 쿼리로 특정 유저가 받은 최신 3개의 리뷰 가져오기
    @Query(value = "SELECT * FROM accompany_review r WHERE r.target_id = :memberId ORDER BY r.created_at DESC LIMIT 3", nativeQuery = true)
    List<AccompanyReviewEntity> findRecentReviewsByTargetId(@Param("memberId") Long memberId);

    // 특정 유저가 받은 모든 리뷰를 가져오기
    List<AccompanyReviewEntity> findAllByTargetId(Long memberId);

    // 특정 유저가 받은 리뷰의 개수 가져오기
    int countByTargetId(Long memberId);
    
    List<Long> findAllTargetIdsByReviewerIdAndChatRoomId(Long reviewerId, Long chatRoomId);
}
