package connectripbe.connectrip_be.review.dto;

import connectripbe.connectrip_be.review.entity.AccompanyReviewEntity;
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
    private int reviewCount; // 리뷰 개수 추가
    private String createdAt;

    // 엔티티를 DTO로 변환하는 메서드
    public static AccompanyReviewResponse fromEntity(AccompanyReviewEntity review, int reviewCount) {
        return AccompanyReviewResponse.builder()
                .reviewId(review.getId())
                .content(review.getContent())
                .reviewerNickname(review.getReviewer().getNickname()) // 작성자 닉네임
                .targetNickname(review.getTarget().getNickname()) // 대상자 닉네임
                .reviewerProfile(review.getReviewer().getProfileImagePath()) // 작성자 프로필 이미지 경로
                .reviewerId(review.getReviewer().getId()) // 작성자 ID
                .targetId(review.getTarget().getId()) // 대상자 ID
                .reviewCount(reviewCount) // 리뷰 수 추가
                .createdAt(formatToUTC(review.getCreatedAt())) // 작성일
                .build();
    }

    // UTC 형식으로 변환하는 메서드 (AccompanyCommentResponse와 동일하게 변경)
    private static final DateTimeFormatter UTC_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static String formatToUTC(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.atZone(ZoneId.systemDefault()) // 시스템 시간대 적용
                .format(UTC_FORMATTER); // 형식에 맞춰 반환
    }
}

