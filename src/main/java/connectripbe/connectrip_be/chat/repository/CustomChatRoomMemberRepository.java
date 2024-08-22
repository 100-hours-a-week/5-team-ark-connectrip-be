package connectripbe.connectrip_be.chat.repository;

import connectripbe.connectrip_be.chat.entity.ChatRoomMemberEntity;
import java.util.List;

public interface CustomChatRoomMemberRepository {

    List<ChatRoomMemberEntity> myChatRoomList(String email);

}
