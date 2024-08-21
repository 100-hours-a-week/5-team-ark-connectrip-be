package connectripbe.connectrip_be.chat.dto;

import connectripbe.connectrip_be.chat.entity.ChatRoomEntity;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import lombok.Builder;

@Builder
public record ChatRoomListResponse(
        Long chatRoomId,
        Long accompanyPostId,
        String accompanyPostTitle,
        String accompanyArea,
        String startDate,
        String endDate,
        String lastChatMessage,
        String lastChatMessageTime
        ) {


      public static ChatRoomListResponse fromEntity(ChatRoomEntity chatRoom){
            return ChatRoomListResponse.builder()
                    .chatRoomId(chatRoom.getId())
                    .accompanyPostId(chatRoom.getAccompanyPost().getId())
                    .accompanyPostTitle(chatRoom.getAccompanyPost().getTitle())
                    .accompanyArea(chatRoom.getAccompanyPost().getAccompanyArea().toString())
                    .startDate(formatToUTC(chatRoom.getAccompanyPost().getStartDate().atStartOfDay()))
                    .endDate(formatToUTC(chatRoom.getAccompanyPost().getEndDate().atStartOfDay()))
                    .lastChatMessage(chatRoom.getLastChatMessage())
                    .lastChatMessageTime(formatToUTC(chatRoom.getLastChatTime()))
                    .build();
      }

      private static final DateTimeFormatter UTC_FORMATTER = DateTimeFormatter.ofPattern(
              "yyyy-MM-dd'T'HH:mm:ss'Z'");

      private static String formatToUTC(LocalDateTime dateTime) {
            if (dateTime == null) {
                  return null;
            }
            return dateTime.atZone(ZoneId.systemDefault())
                    .withZoneSameInstant(ZoneId.of("UTC"))
                    .format(UTC_FORMATTER);
      }
}
