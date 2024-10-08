package connectripbe.connectrip_be.chat.service;

import connectripbe.connectrip_be.chat.entity.ChatRoomEntity;
import connectripbe.connectrip_be.chat.entity.ChatRoomMemberEntity;
import connectripbe.connectrip_be.member.entity.MemberEntity;

public interface ChatRoomMemberService {

    ChatRoomMemberEntity jointChatRoom(ChatRoomEntity chatRoom, MemberEntity member);

}
