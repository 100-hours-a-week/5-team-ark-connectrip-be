package connectripbe.connectrip_be.chat.service.impl;

import connectripbe.connectrip_be.chat.entity.ChatRoomEntity;
import connectripbe.connectrip_be.chat.entity.ChatRoomMemberEntity;
import connectripbe.connectrip_be.chat.entity.type.ChatRoomMemberStatus;
import connectripbe.connectrip_be.chat.repository.ChatRoomMemberRepository;
import connectripbe.connectrip_be.chat.repository.ChatRoomRepository;
import connectripbe.connectrip_be.chat.service.ChatRoomMemberService;
import connectripbe.connectrip_be.global.exception.GlobalException;
import connectripbe.connectrip_be.global.exception.type.ErrorCode;
import connectripbe.connectrip_be.member.entity.MemberEntity;
import connectripbe.connectrip_be.member.repository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomMemberServiceImpl implements ChatRoomMemberService {

    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberJpaRepository memberRepository;

    /**
     * 사용자를 특정 채팅방에 참여
     * 채팅방과 사용자가 존재하는지 확인한 후, 사용자가 이미 채팅방에 참여 중인지 확인
     * 참여하지 않은 경우 채팅방에 사용자를 추가하고, 참여한 경우 예외를 발생
     *
     * @param chatRoomId  참여할 채팅방의 ID
     * @param memberId  참여할 사용자의 ID
     * @throws GlobalException  채팅방이 존재하지 않거나, 사용자가 존재하지 않거나,
     *                          사용자가 이미 채팅방에 참여한 경우 발생하는 예외
     */
    @Override
    public void jointChatRoom(Long chatRoomId, Long memberId) {

        // 채팅방이 존재하는지 확인
        ChatRoomEntity chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new GlobalException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        // 사용자가 존재하는지 확인
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));

        // 이미 채팅방에 참여중인지 확인
        boolean isMemberAlreadyInRoom = chatRoom.getChatRoomMembers().stream()
                .anyMatch(members -> members.getId().equals(memberId));

        if (isMemberAlreadyInRoom) {
            throw new GlobalException(ErrorCode.ALREADY_JOINED_CHAT_ROOM);
        }

        ChatRoomMemberEntity chatRoomMember = ChatRoomMemberEntity.builder()
                .chatRoom(chatRoom)
                .member(member)
                .status(ChatRoomMemberStatus.ACTIVE)
                .build();

        chatRoom.addChatRoomMember(chatRoomMember);
        chatRoomMemberRepository.save(chatRoomMember);

    }
}
