package connectripbe.connectrip_be.communitypost.dto;

import connectripbe.connectrip_be.communitypost.entity.CommunityPostEntity;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityPostResponse {

    private Long id;
    private Long memberId;
    private String title;
    private String content;

    private String createdAt;
    private String updatedAt;

    private static final DateTimeFormatter UTC_FORMATTER = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static String formatToUTC(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.atZone(ZoneId.systemDefault())
                .format(UTC_FORMATTER);
    }

    public static CommunityPostResponse fromEntity(CommunityPostEntity entity) {
        return CommunityPostResponse.builder()
                .id(entity.getId())
                .memberId(entity.getMemberEntity().getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .createdAt(formatToUTC(entity.getCreatedAt()))
                .updatedAt(formatToUTC(entity.getUpdatedAt()))
                .build();
    }
}
