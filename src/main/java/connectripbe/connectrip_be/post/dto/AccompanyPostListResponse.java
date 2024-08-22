package connectripbe.connectrip_be.post.dto;

import static org.springframework.util.StringUtils.truncate;

import connectripbe.connectrip_be.post.entity.AccompanyPostEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import lombok.Builder;

@Builder
public record AccompanyPostListResponse(
        Long id,
        Long memberId,
        String nickname,
        String title,
        String startDate,
        String endDate,
        String accompanyArea,
        String content,
        String createdAt,
        String profileImagePath
) {
      public static AccompanyPostListResponse fromEntity(AccompanyPostEntity accompanyPost){
            return AccompanyPostListResponse.builder()
                    .id(accompanyPost.getId())
                    .memberId(accompanyPost.getMemberEntity().getId())
                    .nickname(accompanyPost.getMemberEntity().getNickname())
                    .title(truncate(accompanyPost.getTitle(), 21))
                    .startDate(formatToUTC(accompanyPost.getStartDate()))
                    .endDate(formatToUTC(accompanyPost.getEndDate()))
                    .accompanyArea(accompanyPost.getAccompanyArea().toString())
                    .content(truncate(accompanyPost.getContent(), 36))
                    .createdAt(formatToUTC(accompanyPost.getCreatedAt()))
                    .profileImagePath(accompanyPost.getMemberEntity().getProfileImagePath())
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
