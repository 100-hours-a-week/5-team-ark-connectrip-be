package connectripbe.connectrip_be.chat.dto;

import connectripbe.connectrip_be.chat.entity.ChatMessage;
import connectripbe.connectrip_be.chat.entity.type.MessageType;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ChatMessageResponse(
        String id,
        MessageType type,
        Long chatRoomId,
        Long senderId,
        String senderNickname,
        String senderProfileImage,
        String content,
        Boolean messageFlag,
        LocalDateTime createdAt
) {
    public static ChatMessageResponse fromEntity(ChatMessage chatMessage) {
        return ChatMessageResponse.builder()
                .id(chatMessage.getId())
                .type(chatMessage.getType())
                .chatRoomId(chatMessage.getChatRoomId())
                .senderId(chatMessage.getSenderId())
                .senderNickname(chatMessage.getSenderNickname())
                .senderProfileImage(chatMessage.getSenderProfileImage())
                .content(chatMessage.getContent())
                .messageFlag(chatMessage.isMessageFlag())
                .createdAt(chatMessage.getCreatedAt())
                .build();
    }
}
