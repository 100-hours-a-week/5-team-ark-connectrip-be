package connectripbe.connectrip_be.post.dto;

import connectripbe.connectrip_be.chat.entity.ChatRoomEntity;
import connectripbe.connectrip_be.global.util.time.DateTimeUtils;
import connectripbe.connectrip_be.post.entity.AccompanyPostEntity;
import lombok.Builder;

@Builder
public record AccompanyPostResponse(
        Long id,
        Long memberId,
        Long leaderId,
        String nickname,
        String profileImagePath,
        String title,
        String startDate,
        String endDate,
        String accompanyArea,
        String customUrl,
        String urlQrPath,
        String content,
        String status,
        String createdAt
) {

    public static AccompanyPostResponse fromEntity(AccompanyPostEntity accompanyPost, String status
            , ChatRoomEntity chatRoom) {

        return AccompanyPostResponse.builder()
                .id(accompanyPost.getId())
                .memberId(accompanyPost.getMemberEntity().getId())
                .leaderId(chatRoom.getCurrentLeader().getMember().getId())
                .nickname(accompanyPost.getMemberEntity().getNickname())
                .profileImagePath(accompanyPost.getMemberEntity().getProfileImagePath())
                .title(accompanyPost.getTitle())
                .startDate(DateTimeUtils.formatUTC(accompanyPost.getStartDate()))
                .endDate(DateTimeUtils.formatUTC(accompanyPost.getEndDate()))
                .accompanyArea(accompanyPost.getAccompanyArea())
                .customUrl(accompanyPost.getCustomUrl())
                .urlQrPath(accompanyPost.getUrlQrPath())
                .content(accompanyPost.getContent())
                .status(status)
                .createdAt(DateTimeUtils.formatUTC(accompanyPost.getCreatedAt()))
                .build();

    }


}
