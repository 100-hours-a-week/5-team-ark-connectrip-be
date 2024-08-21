package connectripbe.connectrip_be.comment.dto;

import connectripbe.connectrip_be.comment.entity.AccompanyCommentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccompanyCommentResponse {

    private Long id;
    private Long memberId;
    private Long accompanyPostId;
    private String content;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;

    // 엔티티를 DTO로 변환하는 메서드
    public static AccompanyCommentResponse fromEntity(AccompanyCommentEntity comment) {
        return AccompanyCommentResponse.builder()
                .id(comment.getId())
                .memberId(comment.getMemberEntity().getId())
                .accompanyPostId(comment.getAccompanyPostEntity().getId())
                .content(comment.getContent())
                .createdAt(formatToUTC(comment.getCreatedAt()))
                .updatedAt(formatToUTC(comment.getUpdatedAt()))
                .deletedAt(formatToUTC(comment.getDeletedAt()))
                .build();
    }

    // UTC 형식으로 변환하는 메서드 추가
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
