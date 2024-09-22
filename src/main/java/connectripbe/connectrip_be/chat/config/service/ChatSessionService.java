package connectripbe.connectrip_be.chat.config.service;

import connectripbe.connectrip_be.chat.dto.ChatRoomSessionDto;
import connectripbe.connectrip_be.chat.entity.ChatRoomEntity;
import connectripbe.connectrip_be.chat.repository.ChatMessageRepository;
import connectripbe.connectrip_be.chat.repository.ChatRoomMemberRepository;
import connectripbe.connectrip_be.chat.repository.ChatRoomRepository;
import connectripbe.connectrip_be.global.exception.GlobalException;
import connectripbe.connectrip_be.global.exception.type.ErrorCode;
import connectripbe.connectrip_be.global.service.RedisService;
import connectripbe.connectrip_be.member.entity.MemberEntity;
import connectripbe.connectrip_be.member.repository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatSessionService {

    private static final String CHAT_ROOM_KEY_PREFIX = "chat_room_session: ";
    private static final String CHAT_ROOM_LIST_KEY_PREFIX = "chat_room_list: ";


    private final RedisService redisService;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final MemberJpaRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;

    // 세션 저장 로직
    public void saveUserSession(Long chatRoomId, Long memberId, String sessionId) {

        ChatRoomSessionDto sessionDto = new ChatRoomSessionDto(chatRoomId, memberId);
        redisService.setClassData(CHAT_ROOM_KEY_PREFIX + sessionId, sessionDto);
        redisService.setListData(CHAT_ROOM_LIST_KEY_PREFIX + chatRoomId, memberId);
    }

    // 세션 삭제 로직
    public void removeUserSession(String sessionId, Long chatRoomId) {
        redisService.deleteData(CHAT_ROOM_KEY_PREFIX + sessionId);
        redisService.deleteListData(CHAT_ROOM_LIST_KEY_PREFIX + chatRoomId, sessionId);
    }

    // 세션 조회 로직
    public ChatRoomSessionDto getUserSession(String sessionId) {
        return redisService.getClassData(CHAT_ROOM_KEY_PREFIX + sessionId, ChatRoomSessionDto.class);
    }

    // 마지막 메시지 업데이트 로직
    public void updateLastReadMessage(ChatRoomSessionDto sessionDto) {
        MemberEntity chatMember = memberRepository.findById(sessionDto.memberId())
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));
        ChatRoomEntity chatRoom = chatRoomRepository.findById(sessionDto.chatRoomId())
                .orElseThrow(() -> new GlobalException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        chatMessageRepository.findTopByChatRoomIdOrderByCreatedAtDesc(chatRoom.getId())
                .ifPresent(chatMessage -> {
                    chatRoomMemberRepository.findByChatRoom_IdAndMember_Id(chatRoom.getId(), chatMember.getId())
                            .ifPresent(member -> {
                                member.updateLastReadMessageId(chatMessage.getId());
                                chatRoomMemberRepository.save(member);
                            });
                });
    }
}
