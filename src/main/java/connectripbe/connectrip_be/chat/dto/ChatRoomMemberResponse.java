package connectripbe.connectrip_be.chat.dto;


import connectripbe.connectrip_be.chat.entity.ChatRoomMemberEntity;
import lombok.Builder;


@Builder
public record ChatRoomMemberResponse(
        Long chatRoomId,
        Long memberId,
        String memberNickname,
        String memberProfileImage,
        String memberChatRoomStatus
) {

    public static ChatRoomMemberResponse fromEntity(ChatRoomMemberEntity chatRoomMember) {
        return ChatRoomMemberResponse.builder()
                .chatRoomId(chatRoomMember.getChatRoom().getId())
                .memberId(chatRoomMember.getMember().getId())
                .memberNickname(chatRoomMember.getMember().getNickname())
                .memberProfileImage(chatRoomMember.getMember().getProfileImagePath())
                .memberChatRoomStatus(chatRoomMember.getStatus().toString())
                .build();
    }
}
