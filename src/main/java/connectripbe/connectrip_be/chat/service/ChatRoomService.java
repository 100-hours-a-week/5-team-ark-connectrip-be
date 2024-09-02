package connectripbe.connectrip_be.chat.service;

import connectripbe.connectrip_be.chat.dto.ChatRoomEnterDto;
import connectripbe.connectrip_be.chat.dto.ChatRoomListResponse;
import connectripbe.connectrip_be.chat.dto.ChatRoomMemberResponse;
import java.util.List;

public interface ChatRoomService {

    List<ChatRoomListResponse> getChatRoomList(Long memberId);

    List<ChatRoomMemberResponse> getChatRoomMembers(Long chatRoomId);

    void createChatRoom(Long postId, Long memberId);

    void exitChatRoom(Long chatRoomId, Long id);

    ChatRoomEnterDto enterChatRoom(Long chatRoomId, Long memberId);

}
