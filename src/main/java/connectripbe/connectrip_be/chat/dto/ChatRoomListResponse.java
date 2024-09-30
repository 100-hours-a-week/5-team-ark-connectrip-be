package connectripbe.connectrip_be.chat.dto;

import connectripbe.connectrip_be.chat.entity.ChatRoomEntity;
import connectripbe.connectrip_be.chat.entity.type.ChatRoomMemberStatus;
import connectripbe.connectrip_be.global.util.time.DateTimeUtils;
import java.time.LocalDateTime;
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
        int memberNumber,
        boolean hasUnreadMessages
) {


    public static ChatRoomListResponse fromEntity(ChatRoomEntity chatRoom, boolean hasUnreadMessages) {
        // 마지막 채팅 시간이 없을 경우 생성 시간으로 대체
        LocalDateTime lastChatTime = chatRoom.getLastChatTime() != null
                ? chatRoom.getLastChatTime()
                : chatRoom.getCreatedAt();

        // 활성 멤버 수 계산
        int activeMemberCnt = (int) chatRoom.getChatRoomMembers().stream()
                .filter(member -> member.getStatus().equals(ChatRoomMemberStatus.ACTIVE))
                .count();

        return ChatRoomListResponse.builder()
                .chatRoomId(chatRoom.getId())
                .accompanyPostId(chatRoom.getAccompanyPost().getId())
                .accompanyPostTitle(chatRoom.getAccompanyPost().getTitle())
                .accompanyArea(chatRoom.getAccompanyPost().getAccompanyArea())
                .startDate(DateTimeUtils.formatUTC(chatRoom.getAccompanyPost().getStartDate()))
                .endDate(DateTimeUtils.formatUTC(chatRoom.getAccompanyPost().getEndDate()))
                .lastChatMessage(chatRoom.getLastChatMessage())
                .lastChatMessageTime(DateTimeUtils.formatUTC(lastChatTime))
                .memberNumber(activeMemberCnt)
                .hasUnreadMessages(hasUnreadMessages)
                .build();
    }

}
