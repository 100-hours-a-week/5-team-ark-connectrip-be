package connectripbe.connectrip_be.member.dto;

import connectripbe.connectrip_be.member.entity.MemberEntity;
import lombok.Builder;

@Builder
public record MemberHeaderInfoDto(
        Long memberId,
        String profileImagePath,
        String nickname
) {
    public static MemberHeaderInfoDto fromEntity(MemberEntity memberEntity) {
        return MemberHeaderInfoDto.builder()
                .memberId(memberEntity.getId())
                .profileImagePath(memberEntity.getProfileImagePath())
                .nickname(memberEntity.getNickname())
                .build();
    }
}
