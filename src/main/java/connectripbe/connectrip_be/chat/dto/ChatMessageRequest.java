package connectripbe.connectrip_be.chat.dto;


import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record ChatMessageRequest(
        Long chatRoomId,
        String senderId,
        @Size(max = 256, message = "채팅 내용은 256자 이내로 입력해주세요.")
        String content,
        Boolean infoFlag
) {
}
