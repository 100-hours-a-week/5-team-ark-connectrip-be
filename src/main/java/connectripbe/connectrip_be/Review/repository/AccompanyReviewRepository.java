package connectripbe.connectrip_be.Review.repository;

import connectripbe.connectrip_be.Review.entity.AccompanyReviewEntity;
import connectripbe.connectrip_be.chat.entity.ChatRoomEntity;
import connectripbe.connectrip_be.member.entity.MemberEntity;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccompanyReviewRepository extends JpaRepository<AccompanyReviewEntity, Long> {

    // 특정 채팅방에 대한 리뷰 가져오기
    List<AccompanyReviewEntity> findByChatRoomId(Long chatRoomId);

    // 리뷰어, 대상자, 채팅방을 기준으로 리뷰 존재 여부 확인
    boolean existsByReviewerAndTargetAndChatRoom(MemberEntity reviewer, MemberEntity target, ChatRoomEntity chatRoom);

    // 특정 유저가 받은 최신 3개의 리뷰 가져오기 (Pageable을 사용해 LIMIT 적용)
    List<AccompanyReviewEntity> findRecentReviewsByTargetId(Long memberId, Pageable pageable);

    // 특정 유저가 받은 모든 리뷰를 가져오기
    List<AccompanyReviewEntity> findAllByTargetId(Long memberId);

    // 특정 유저가 받은 리뷰의 개수 가져오기 (Spring Data JPA가 자동으로 COUNT 쿼리 생성)
    int countByTargetId(Long memberId);
}
