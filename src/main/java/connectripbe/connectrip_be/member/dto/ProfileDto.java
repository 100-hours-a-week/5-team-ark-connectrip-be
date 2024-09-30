package connectripbe.connectrip_be.member.dto;

import connectripbe.connectrip_be.global.util.time.DateTimeUtils;
import connectripbe.connectrip_be.member.entity.MemberEntity;
import connectripbe.connectrip_be.review.dto.response.AccompanyReviewResponse;
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
                .createdAt(DateTimeUtils.formatUTC(member.getCreatedAt()))
                .build();
    }


}
