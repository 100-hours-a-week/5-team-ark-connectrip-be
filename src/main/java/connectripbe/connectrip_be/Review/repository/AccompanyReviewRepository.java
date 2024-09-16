package connectripbe.connectrip_be.Review.repository;

import connectripbe.connectrip_be.Review.entity.AccompanyReviewEntity;
import connectripbe.connectrip_be.chat.entity.ChatRoomEntity;
import connectripbe.connectrip_be.member.entity.MemberEntity;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccompanyReviewRepository extends JpaRepository<AccompanyReviewEntity, Long> {
    List<AccompanyReviewEntity> findByChatRoomId(Long chatRoomId);

    boolean existsByReviewerAndTargetAndChatRoom(MemberEntity reviewer, MemberEntity target, ChatRoomEntity chatRoom);

    // 특정 유저가 받은 최신 3개의 리뷰 가져오기 (Pageable을 사용해 LIMIT 적용)
    @Query("SELECT r FROM AccompanyReviewEntity r WHERE r.target.id = :memberId ORDER BY r.createdAt DESC")
    List<AccompanyReviewEntity> findRecentReviewsByTargetId(Long memberId, Pageable pageable);


    // 특정 유저가 받은 모든 리뷰를 가져오기
    @Query("SELECT r FROM AccompanyReviewEntity r WHERE r.target.id = :memberId ORDER BY r.createdAt DESC")
    List<AccompanyReviewEntity> findAllByTargetId(Long memberId);


    // 특정 유저가 받은 리뷰의 개수 가져오기 (리뷰 수 계산)
    @Query("SELECT COUNT(r) FROM AccompanyReviewEntity r WHERE r.target.id = :memberId")
    int countReviewsByTargetId(Long memberId);

}
