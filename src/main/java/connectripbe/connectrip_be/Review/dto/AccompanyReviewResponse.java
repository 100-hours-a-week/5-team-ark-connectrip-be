package connectripbe.connectrip_be.Review.dto;

import connectripbe.connectrip_be.Review.entity.AccompanyReviewEntity;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccompanyReviewResponse {
    private Long reviewId;
    private String content;
    private String reviewerNickname;
    private String targetNickname;
    private String reviewerProfile;
    private Long reviewerId;
    private Long targetId;
    private int reviewCount;
    private String createdAt;

    // 엔티티를 DTO로 변환하는 메서드
    public static AccompanyReviewResponse fromEntity(AccompanyReviewEntity review, int reviewCount) {
        return AccompanyReviewResponse.builder()
                .reviewId(review.getId())
                .content(review.getContent())
                .reviewerNickname(review.getReviewer().getNickname()) // 작성자 닉네임
                .targetNickname(review.getTarget().getNickname())
                .reviewerProfile(review.getReviewer().getProfileImagePath()) // 작성자 프로필 이미지 경로
                .reviewerId(review.getReviewer().getId()) // 작성자 ID
                .targetId(review.getTarget().getId())
                .reviewCount(reviewCount) // 전체 리뷰 수 추가
                .createdAt(formatToUTC(review.getCreatedAt())) // 작성일
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
