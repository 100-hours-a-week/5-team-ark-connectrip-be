package connectripbe.connectrip_be.chat.dto;

import connectripbe.connectrip_be.chat.entity.ChatRoomEntity;
import lombok.Builder;

@Builder
public record ChatRoomEnterDto(
        Long accompanyPostId,
        Long chatRoomId,
        Long leaderId,
        String status,
        boolean isPostDeleted
) {

    public static ChatRoomEnterDto fromEntity(ChatRoomEntity chatRoom, String status, boolean isPostDeleted) {
        return ChatRoomEnterDto.builder()
                .accompanyPostId(chatRoom.getAccompanyPost().getId())
                .chatRoomId(chatRoom.getId())
                .leaderId(chatRoom.getCurrentLeader().getMember().getId())
                .status(status)
                .isPostDeleted(isPostDeleted)
                .build();
    }
}
