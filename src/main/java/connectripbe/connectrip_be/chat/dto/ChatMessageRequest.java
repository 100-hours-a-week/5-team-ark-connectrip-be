package connectripbe.connectrip_be.chat.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import connectripbe.connectrip_be.global.converter.StringToLongDeserializer;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record ChatMessageRequest(
        Long chatRoomId,
        @JsonDeserialize(using = StringToLongDeserializer.class) // 커스텀 역직렬화기 사용
        Long senderId,
        @Size(max = 256, message = "채팅 내용은 256자 이내로 입력해주세요.")
        String content,
        Boolean infoFlag
) {
}
