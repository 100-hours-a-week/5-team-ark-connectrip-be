package connectripbe.connectrip_be.chat.service.impl;

import connectripbe.connectrip_be.chat.dto.ChatRoomListResponse;
import connectripbe.connectrip_be.chat.entity.ChatRoomMemberEntity;

import connectripbe.connectrip_be.chat.repository.ChatRoomMemberRepository;
import connectripbe.connectrip_be.chat.service.ChatRoomService;


import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

      private final ChatRoomMemberRepository chatRoomMemberRepository;

      /**
       * 사용자가 참여한 채팅방 목록을 조회하여 반환하는 메서드. 주어진 사용자의 이메일 주소를 기반으로 해당 사용자가 참여한 모든 채팅방을 조회
       *
       * @param email 사용자의 이메일 주소. 이 이메일을 기반으로 해당 사용자가 참여한 채팅방을 조회
       * @return 사용자가 참여한 채팅방의 목록을 포함하는 `List<ChatRoomListResponse>` 사용자가 참여한 채팅방이 없을 경우 빈 리스트를 반환
       */
      @Override
      public List<ChatRoomListResponse> getChatRoomList(String email) {
            // 사용자 참여한 모든 ChatRoomMemberEntity 조회
            List<ChatRoomMemberEntity> chatRoomMembers =  chatRoomMemberRepository.myChatRoomList(email);

            return chatRoomMembers.stream()
                    .map(member -> ChatRoomListResponse.fromEntity(member.getChatRoom()))
                    .toList();
      }


}
