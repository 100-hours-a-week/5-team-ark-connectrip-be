package connectripbe.connectrip_be.post.dto;

import connectripbe.connectrip_be.chat.entity.ChatRoomEntity;
import connectripbe.connectrip_be.post.entity.AccompanyPostEntity;
import lombok.Builder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

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
                .startDate(formatToUTC(accompanyPost.getStartDate()))
                .endDate(formatToUTC(accompanyPost.getEndDate()))
                .accompanyArea(accompanyPost.getAccompanyArea())
                .customUrl(accompanyPost.getCustomUrl())
                .urlQrPath(accompanyPost.getUrlQrPath())
                .content(accompanyPost.getContent())
                .status(status)
                .createdAt(formatToUTC(accompanyPost.getCreatedAt()))
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
