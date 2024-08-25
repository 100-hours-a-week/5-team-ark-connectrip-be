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
        String lastChatMessageTime,
        int memberNumber
) {


    public static ChatRoomListResponse fromEntity(ChatRoomEntity chatRoom) {
        // 마지막 채팅 시간이 없을 경우 생성 시간으로 대체
        LocalDateTime lastChatTime = chatRoom.getLastChatTime() != null
                ? chatRoom.getLastChatTime()
                : chatRoom.getCreatedAt();

        return ChatRoomListResponse.builder()
                .chatRoomId(chatRoom.getId())
                .accompanyPostId(chatRoom.getAccompanyPost().getId())
                .accompanyPostTitle(chatRoom.getAccompanyPost().getTitle())
                .accompanyArea(chatRoom.getAccompanyPost().getAccompanyArea())
                .startDate(formatToUTC(chatRoom.getAccompanyPost().getStartDate()))
                .endDate(formatToUTC(chatRoom.getAccompanyPost().getEndDate()))
                .lastChatMessage(chatRoom.getLastChatMessage())
                .lastChatMessageTime(formatToUTC(lastChatTime))
                .memberNumber(chatRoom.getChatRoomMembers().size())
                .build();
    }

    private static final DateTimeFormatter UTC_FORMATTER = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static String formatToUTC(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.atZone(ZoneId.systemDefault())
                .format(UTC_FORMATTER);
    }
}
