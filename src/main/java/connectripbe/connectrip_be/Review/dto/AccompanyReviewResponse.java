package connectripbe.connectrip_be.Review.dto;

import connectripbe.connectrip_be.Review.entity.AccompanyReviewEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
public class AccompanyReviewResponse {
    private Long reviewId;
    private Long reviewerId;
    private Long targetId;
    private Long chatRoomId;
    private String content;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;

    // 엔티티를 DTO로 변환하는 메서드
    public static AccompanyReviewResponse fromEntity(AccompanyReviewEntity review) {
        return AccompanyReviewResponse.builder()
                .reviewId(review.getId())
                .reviewerId(review.getReviewer().getId())
                .targetId(review.getTarget().getId())
                .chatRoomId(review.getChatRoom().getId())
                .content(review.getContent())
                .createdAt(formatToUTC(review.getCreatedAt()))
                .updatedAt(formatToUTC(review.getUpdatedAt()))
                .deletedAt(formatToUTC(review.getDeletedAt()))
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
