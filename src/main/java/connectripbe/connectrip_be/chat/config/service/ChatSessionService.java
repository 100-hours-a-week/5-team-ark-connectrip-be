package connectripbe.connectrip_be.chat.config.service;

import connectripbe.connectrip_be.chat.dto.ChatRoomSessionDto;
import connectripbe.connectrip_be.chat.repository.ChatMessageRepository;
import connectripbe.connectrip_be.chat.repository.ChatRoomMemberRepository;
import connectripbe.connectrip_be.global.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatSessionService {

    private static final String CHAT_ROOM_KEY_PREFIX = "chat_room_session:";


    private final RedisService redisService;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    // 세션 저장 로직
    public void saveUserSession(Long chatRoomId, Long memberId, String sessionId) {

        ChatRoomSessionDto sessionDto = new ChatRoomSessionDto(chatRoomId, memberId);

        redisService.updateToHash(CHAT_ROOM_KEY_PREFIX, sessionId, sessionDto);
    }

    // 세션 삭제 로직
    public void removeUserSession(String sessionId) {
        redisService.deleteHashKey(CHAT_ROOM_KEY_PREFIX, sessionId);
    }

    // 세션 조회 로직
    public ChatRoomSessionDto getUserSession(String sessionId) {
        return redisService.getClassData(CHAT_ROOM_KEY_PREFIX, sessionId, ChatRoomSessionDto.class);
    }

    // 마지막 메시지 업데이트 로직
    public void updateLastReadMessage(Long memberId, Long chatRoomId) {
        chatMessageRepository.findTopByChatRoomIdOrderByCreatedAtDesc(chatRoomId)
                .ifPresent(chatMessage -> {
                    chatRoomMemberRepository.findByChatRoom_IdAndMember_Id(chatRoomId, memberId)
                            .ifPresent(member -> {
                                member.updateLastReadMessageId(chatMessage.getId());

                                chatRoomMemberRepository.save(member);
                            });
                });
    }
}
