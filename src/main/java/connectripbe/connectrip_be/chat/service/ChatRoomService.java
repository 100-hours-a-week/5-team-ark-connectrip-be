package connectripbe.connectrip_be.chat.service;

import connectripbe.connectrip_be.accompany.post.entity.AccompanyPostEntity;
import connectripbe.connectrip_be.chat.dto.ChatRoomEnterDto;
import connectripbe.connectrip_be.chat.dto.ChatRoomListResponse;
import connectripbe.connectrip_be.chat.dto.ChatRoomMemberResponse;
import connectripbe.connectrip_be.chat.dto.ChatUnreadMessagesResponse;
import connectripbe.connectrip_be.member.entity.MemberEntity;
import java.util.List;

public interface ChatRoomService {

    List<ChatRoomListResponse> getChatRoomList(Long memberId);

    List<ChatRoomMemberResponse> getChatRoomMembers(Long chatRoomId, Long memberId);

    void createChatRoom(AccompanyPostEntity postEntity, MemberEntity memberEntity);

    void exitChatRoom(Long chatRoomId, Long id);

    ChatRoomEnterDto enterChatRoom(Long chatRoomId, Long memberId);

    ChatUnreadMessagesResponse hasUnreadMessagesAcrossRooms(Long memberId);
}
