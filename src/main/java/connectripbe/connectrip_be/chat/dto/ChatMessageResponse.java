package connectripbe.connectrip_be.chat.dto;

import connectripbe.connectrip_be.chat.entity.ChatMessage;
import connectripbe.connectrip_be.chat.entity.type.MessageType;
import connectripbe.connectrip_be.global.util.time.DateTimeUtils;
import lombok.Builder;

@Builder
public record ChatMessageResponse(
        String id,
        MessageType type,
        Long chatRoomId,
        Long senderId,
        String chatRoomTitle,
        String senderNickname,
        String senderProfileImage,
        String content,
        Boolean infoFlag,
        String createdAt
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
                .infoFlag(chatMessage.isInfoFlag())
                .createdAt(DateTimeUtils.formatUTC(chatMessage.getCreatedAt()))
                .build();
    }

    public static ChatMessageResponse notificationFromEntity(ChatMessage chatMessage, String title) {
        if (title.length() > 15) {
            title = title.substring(0, 15);
        }
        String content = chatMessage.getContent();
        if (content.length() > 20) {
            content = content.substring(0, 20);
        }
        return ChatMessageResponse.builder()
                .id(chatMessage.getId())
                .type(chatMessage.getType())
                .chatRoomId(chatMessage.getChatRoomId())
                .senderId(chatMessage.getSenderId())
                .chatRoomTitle(title)
                .senderNickname(chatMessage.getSenderNickname())
                .senderProfileImage(chatMessage.getSenderProfileImage())
                .content(content)
                .infoFlag(chatMessage.isInfoFlag())
                .createdAt(DateTimeUtils.formatUTC(chatMessage.getCreatedAt()))
                .build();
    }

}
