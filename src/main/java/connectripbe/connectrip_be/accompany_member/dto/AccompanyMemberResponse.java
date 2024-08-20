package connectripbe.connectrip_be.accompany_member.dto;

import connectripbe.connectrip_be.accompany_member.entity.AccompanyMemberEntity;
import lombok.Builder;

@Builder
public record AccompanyMemberResponse(
        Long memberId,
        String nickname,
        String profileImagePath,
        String status
) {

      public static AccompanyMemberResponse fromEntity(AccompanyMemberEntity accompanyMember) {
            return AccompanyMemberResponse.builder()
                    .memberId(accompanyMember.getMember().getId())
                    .nickname(accompanyMember.getMember().getNickname())
                    .profileImagePath(accompanyMember.getMember().getProfileImagePath())
                    .status(accompanyMember.getStatus().getDisplayName())
                    .build();
      }
}
