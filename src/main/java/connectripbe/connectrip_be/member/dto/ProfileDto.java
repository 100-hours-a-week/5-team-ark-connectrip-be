package connectripbe.connectrip_be.member.dto;

import connectripbe.connectrip_be.member.entity.MemberEntity;
import connectripbe.connectrip_be.review.dto.AccompanyReviewResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileDto {

    private Long memberId;
    private String profileImagePath;
    private String nickname;
    private String gender;
    private int reviewCount;
    private List<AccompanyReviewResponse> recentReviews;
    private String description;
    private String ageGroup;
    private String createdAt;

    public static ProfileDto fromEntity(MemberEntity member, List<AccompanyReviewResponse> recentReviews,
                                        int reviewCount, String ageGroup) {
        return ProfileDto.builder()
                .memberId(member.getId())
                .profileImagePath(member.getProfileImagePath())
                .nickname(member.getNickname())
                .gender(member.getGender())
                .reviewCount(reviewCount)
                .recentReviews(recentReviews)
                .description(member.getDescription())
                .ageGroup(ageGroup)
                .createdAt(formatToUTC(member.getCreatedAt()))
                .build();
    }

    // UTC 형식으로 변환하는 메서드 추가
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
