package connectripbe.connectrip_be.chat.repository;

import connectripbe.connectrip_be.chat.entity.ChatRoomMemberEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMemberEntity, Long>,
        CustomChatRoomMemberRepository {

    List<ChatRoomMemberEntity> findByMember_Email(String email);

    List<ChatRoomMemberEntity> findByChatRoom_Id(Long chatRoomId);
}
