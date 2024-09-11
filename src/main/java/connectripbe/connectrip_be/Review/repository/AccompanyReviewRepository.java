package connectripbe.connectrip_be.Review.repository;

import connectripbe.connectrip_be.Review.entity.AccompanyReviewEntity;
import connectripbe.connectrip_be.member.entity.MemberEntity;
import connectripbe.connectrip_be.chat.entity.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccompanyReviewRepository extends JpaRepository<AccompanyReviewEntity, Long> {
    List<AccompanyReviewEntity> findByChatRoomId(Long chatRoomId);
    boolean existsByReviewerAndTargetAndChatRoom(MemberEntity reviewer, MemberEntity target, ChatRoomEntity chatRoom);
}
