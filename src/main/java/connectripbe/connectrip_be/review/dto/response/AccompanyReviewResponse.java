package connectripbe.connectrip_be.review.dto.response;

import connectripbe.connectrip_be.global.util.time.DateTimeUtils;
import connectripbe.connectrip_be.review.entity.AccompanyReviewEntity;
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
                .createdAt(DateTimeUtils.formatUTC(review.getCreatedAt())) // 작성일
                .build();
    }


}

