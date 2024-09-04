package connectripbe.connectrip_be.chat.dto;

import lombok.Builder;

@Builder
public record ChatMessageRequest(
        Long chatRoomId,
        Long senderId,
        String content,
        Boolean messageFlag
) {
}
