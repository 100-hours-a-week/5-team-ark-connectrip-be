package connectripbe.connectrip_be.chat.service.impl;

import connectripbe.connectrip_be.chat.dto.ChatMessageDto;
import connectripbe.connectrip_be.chat.entity.ChatMessage;
import connectripbe.connectrip_be.chat.entity.ChatRoomEntity;
import connectripbe.connectrip_be.chat.repository.ChatMessageRepository;
import connectripbe.connectrip_be.chat.repository.ChatRoomRepository;
import connectripbe.connectrip_be.chat.service.ChatMessageService;
import connectripbe.connectrip_be.global.exception.GlobalException;
import connectripbe.connectrip_be.global.exception.type.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    public ChatMessageDto saveMessage(ChatMessageDto chatMessageDto) {
        ChatMessage chatMessage =
                ChatMessage.builder()
                        .id(chatMessageDto.id())
                        .type(chatMessageDto.type())
                        .chatRoomId(chatMessageDto.chatRoomId())
                        .senderId(chatMessageDto.senderId())
                        .senderNickname(chatMessageDto.senderNickname())
                        .senderProfileImage(chatMessageDto.senderProfileImage())
                        .content(chatMessageDto.content())
                        .createdAt(chatMessageDto.createdAt())
                        .build();

        ChatMessage saved = chatMessageRepository.save(chatMessage);

        ChatRoomEntity chatRoom = chatRoomRepository.findById(saved.getChatRoomId())
                .orElseThrow(() -> new GlobalException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        // 채팅방 테이블에 채팅 마지막 내용과 마지막 시간 업데이트
        chatRoom.updateLastChatMessage(saved.getContent(), saved.getCreatedAt());
        chatRoomRepository.save(chatRoom);

        return ChatMessageDto.fromEntity(saved);
    }

    @Override
    public List<ChatMessageDto> getMessages(Long chatRoomId) {
        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomId(chatRoomId);

        return chatMessages.stream()
                .map(ChatMessageDto::fromEntity)
                .toList();
    }
}
