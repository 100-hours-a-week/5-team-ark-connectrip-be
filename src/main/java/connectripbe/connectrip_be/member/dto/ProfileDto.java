package connectripbe.connectrip_be.member.dto;

import connectripbe.connectrip_be.member.entity.MemberEntity;
import connectripbe.connectrip_be.review.dto.AccompanyReviewResponse;
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
    private int accompanyCount;
    private int reviewCount;
    private List<AccompanyReviewResponse> recentReviews;
    private String description;
    private String ageGroup;  // 나이대는 외부에서 전달받음

    public static ProfileDto fromEntity(MemberEntity member, List<AccompanyReviewResponse> recentReviews,
                                        String ageGroup) {
        return ProfileDto.builder()
                .memberId(member.getId())
                .profileImagePath(member.getProfileImagePath())
                .nickname(member.getNickname())
                .gender(member.getGender())
                .accompanyCount(member.getAccompanyCount())
                .recentReviews(recentReviews)
                .description(member.getDescription())
                .ageGroup(ageGroup)  // 서비스 계층에서 계산된 나이대 전달
                .build();
    }
}
