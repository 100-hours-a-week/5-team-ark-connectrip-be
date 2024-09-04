package connectripbe.connectrip_be.chat.dto;

import connectripbe.connectrip_be.chat.entity.ChatRoomMemberEntity;
import lombok.Builder;

@Builder
public record ChatRoomMemberLocationDto(
        Long memberId,
        String profileImagePath,
        String nickname,
        LastLocationDto lastLocationDto
) {

    // info-noah: 1+N 문제 또는 쿼리가 여러개 발생하는지 확인 필요
    public static ChatRoomMemberLocationDto fromEntity(ChatRoomMemberEntity chatRoomMemberEntity) {
        return ChatRoomMemberLocationDto.builder()
                .memberId(chatRoomMemberEntity.getMember().getId())
                .profileImagePath(chatRoomMemberEntity.getMember().getProfileImagePath())
                .nickname(chatRoomMemberEntity.getMember().getNickname())
                .lastLocationDto(new LastLocationDto(
                        chatRoomMemberEntity.getLastLatitude(),
                        chatRoomMemberEntity.getLastLongitude())
                )
                .build();
    }
}
