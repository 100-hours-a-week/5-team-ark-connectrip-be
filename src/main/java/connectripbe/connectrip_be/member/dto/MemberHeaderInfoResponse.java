package connectripbe.connectrip_be.member.dto;

import connectripbe.connectrip_be.member.entity.MemberEntity;
import lombok.Builder;

@Builder
public record MemberHeaderInfoResponse(
        Long memberId,
        String profileImagePath,
        String nickname
) {
    public static MemberHeaderInfoResponse fromEntity(MemberEntity memberEntity) {
        return MemberHeaderInfoResponse.builder()
                .memberId(memberEntity.getId())
                .profileImagePath(memberEntity.getProfileImagePath())
                .nickname(memberEntity.getNickname())
                .build();
    }
}
