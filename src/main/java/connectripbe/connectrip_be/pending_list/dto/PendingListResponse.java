package connectripbe.connectrip_be.pending_list.dto;

import connectripbe.connectrip_be.pending_list.entity.PendingListEntity;
import lombok.Builder;

@Builder
public record PendingListResponse(

        Long accompanyPostId,
        Long memberId,
        String memberNickname,
        String profileImagePath
) {
    public static PendingListResponse fromEntity(PendingListEntity pending) {

        return PendingListResponse.builder()
                .accompanyPostId(pending.getAccompanyPost().getId())
                .memberId(pending.getMember().getId())
                .memberNickname(pending.getMember().getNickname())
                .profileImagePath(pending.getMember().getProfileImagePath())
                .build();
    }
}
