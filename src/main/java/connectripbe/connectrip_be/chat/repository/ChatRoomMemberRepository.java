package connectripbe.connectrip_be.chat.repository;

import connectripbe.connectrip_be.chat.entity.ChatRoomMemberEntity;
import connectripbe.connectrip_be.chat.entity.type.ChatRoomMemberStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMemberEntity, Long>,
        CustomChatRoomMemberRepository {

    List<ChatRoomMemberEntity> findByMember_Email(String email);

    List<ChatRoomMemberEntity> findByChatRoom_Id(Long chatRoomId);

    List<ChatRoomMemberEntity> findAllByChatRoom_IdAndStatusAndIsLocationSharingEnabled(
            Long chatRoomId,
            ChatRoomMemberStatus status,
            boolean isLocationTrackingEnabled);

    Optional<ChatRoomMemberEntity> findByChatRoom_IdAndMember_Id(Long chatRoomId, Long memberId);

    Integer countByChatRoom_IdAndStatus(Long chatRoomId, ChatRoomMemberStatus chatRoomMemberStatus);

    Optional<ChatRoomMemberEntity> findFirstByChatRoom_IdAndStatusOrderByCreatedAt(Long chatRoomId,
                                                                                   ChatRoomMemberStatus chatRoomMemberStatus);

    @Query("SELECT m.id FROM chat_room_member crm "
            + "JOIN crm.member m"
            + " WHERE crm.chatRoom.id = :chatRoomId and crm.status = 'ACTIVE'")
    List<Long> findMemberIdsByChatRoomId(Long chatRoomId);


    boolean existsByChatRoomIdAndMemberId(Long chatRoomId, Long memberId);

}
