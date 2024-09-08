package connectripbe.connectrip_be.chat.dto;

import connectripbe.connectrip_be.chat.entity.ChatMessage;
import connectripbe.connectrip_be.chat.entity.type.MessageType;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
        Boolean infoFlag,
        String createdAt
) {
    private static final DateTimeFormatter UTC_FORMATTER = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd'T'HH:mm:ss'Z'");

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
                .createdAt(formatToUTC(chatMessage.getCreatedAt()))
                .build();
    }

    private static String formatToUTC(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.atZone(ZoneId.systemDefault())
                .format(UTC_FORMATTER);
    }
}
