package connectripbe.connectrip_be.post.dto;

import connectripbe.connectrip_be.post.entity.AccompanyPostEntity;
import connectripbe.connectrip_be.post.entity.enums.AccompanyArea;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Builder
public record AccompanyPostResponse(
        Long id,
        Long memberId,
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

    public static AccompanyPostResponse fromEntity(AccompanyPostEntity accompanyPost) {

        return AccompanyPostResponse.builder()
                .id(accompanyPost.getId())
                .memberId(accompanyPost.getMemberEntity().getId())
                .nickname(accompanyPost.getMemberEntity().getNickname())
                .profileImagePath(accompanyPost.getMemberEntity().getProfileImagePath())
                .title(accompanyPost.getTitle())
                .startDate(formatToUTC(accompanyPost.getStartDate().atStartOfDay()))
                .endDate(formatToUTC(accompanyPost.getEndDate().atStartOfDay()))
                .accompanyArea(accompanyPost.getAccompanyArea().getDisplayName())
                .customUrl(accompanyPost.getCustomUrl())
                .urlQrPath(accompanyPost.getUrlQrPath())
                .content(accompanyPost.getContent())
                .status(accompanyPost.getRequestStatus())
                .createdAt(formatToUTC(accompanyPost.getCreatedAt()))
                .build();

    }

    private static final DateTimeFormatter UTC_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static String formatToUTC(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneId.of("UTC"))
                .format(UTC_FORMATTER);
    }
}
