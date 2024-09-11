package connectripbe.connectrip_be.member.dto;

import connectripbe.connectrip_be.Review.dto.AccompanyReviewResponse;
import connectripbe.connectrip_be.member.entity.MemberEntity;
import java.time.LocalDate;
import java.time.Period;
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
    private String ageGroup;

    public static ProfileDto fromEntity(MemberEntity member, List<AccompanyReviewResponse> recentReviews,
                                        int reviewCount) {
        return ProfileDto.builder()
                .memberId(member.getId())
                .profileImagePath(member.getProfileImagePath())
                .nickname(member.getNickname())
                .gender(member.getGender())
                .accompanyCount(member.getAccompanyCount())
                .reviewCount(reviewCount)
                .recentReviews(recentReviews)
                .description(member.getDescription())
                .ageGroup(calculateAgeGroup(member.getBirthDate().toLocalDate()))
                .build();
    }

    private static int calculateAge(LocalDate birthDate) {
        LocalDate now = LocalDate.now();
        return Period.between(birthDate, now).getYears();
    }

    private static String calculateAgeGroup(LocalDate birthDate) {
        int age = calculateAge(birthDate);
        if (age < 20) {
            return "10대";
        } else if (age < 30) {
            return "20대";
        } else if (age < 40) {
            return "30대";
        } else if (age < 50) {
            return "40대";
        } else if (age < 60) {
            return "50대";
        } else {
            return "60대 이상";
        }
    }
}
