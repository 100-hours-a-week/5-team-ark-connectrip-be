package connectripbe.connectrip_be.chat.service.impl;

import connectripbe.connectrip_be.chat.dto.ChatMessageRequest;
import connectripbe.connectrip_be.chat.dto.ChatMessageResponse;
import connectripbe.connectrip_be.chat.entity.ChatMessage;
import connectripbe.connectrip_be.chat.entity.ChatRoomEntity;
import connectripbe.connectrip_be.chat.entity.type.MessageType;
import connectripbe.connectrip_be.chat.repository.ChatMessageRepository;
import connectripbe.connectrip_be.chat.repository.ChatRoomRepository;
import connectripbe.connectrip_be.chat.service.ChatMessageService;
import connectripbe.connectrip_be.global.exception.GlobalException;
import connectripbe.connectrip_be.global.exception.type.ErrorCode;
import connectripbe.connectrip_be.member.entity.MemberEntity;
import connectripbe.connectrip_be.member.repository.MemberJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberJpaRepository memberJpaRepository;

    @Override
    public ChatMessageResponse saveMessage(ChatMessageRequest request) {
        // 채팅 수신 유저 정보 조회
        MemberEntity member = memberJpaRepository.findById(request.senderId())
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));

        ChatMessage chatMessage =
                ChatMessage.builder()
                        .type(MessageType.TALK)
                        .chatRoomId(request.chatRoomId())
                        .senderId(request.senderId())
                        .senderNickname(member.getNickname())
                        .senderProfileImage(member.getProfileImagePath())
                        .content(request.content())
                        .build();

        ChatMessage saved = chatMessageRepository.save(chatMessage);

        ChatRoomEntity chatRoom = chatRoomRepository.findById(saved.getChatRoomId())
                .orElseThrow(() -> new GlobalException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        // 채팅방 테이블에 채팅 마지막 내용과 마지막 시간 업데이트
        chatRoom.updateLastChatMessage(saved.getContent(), saved.getCreatedAt());
        chatRoomRepository.save(chatRoom);

        return ChatMessageResponse.fromEntity(saved);
    }

    @Override
    public List<ChatMessageResponse> getMessages(Long chatRoomId) {
        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomId(chatRoomId);

        return chatMessages.stream()
                .map(ChatMessageResponse::fromEntity)
                .toList();
    }
}
