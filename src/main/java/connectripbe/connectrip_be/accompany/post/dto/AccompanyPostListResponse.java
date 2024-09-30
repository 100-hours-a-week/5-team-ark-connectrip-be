package connectripbe.connectrip_be.accompany.post.dto;

import static org.springframework.util.StringUtils.truncate;

import connectripbe.connectrip_be.accompany.post.entity.AccompanyPostEntity;
import connectripbe.connectrip_be.global.util.time.DateTimeUtils;
import lombok.Builder;

// todo-noah: 이름 변경, utc 분리
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
    public static AccompanyPostListResponse fromEntity(AccompanyPostEntity accompanyPost) {
        return AccompanyPostListResponse.builder()
                .id(accompanyPost.getId())
                .memberId(accompanyPost.getMemberEntity().getId())
                .nickname(accompanyPost.getMemberEntity().getNickname())
                .title(truncate(accompanyPost.getTitle(), 21))
                .startDate(DateTimeUtils.formatUTC(accompanyPost.getStartDate()))
                .endDate(DateTimeUtils.formatUTC(accompanyPost.getEndDate()))
                .accompanyArea(accompanyPost.getAccompanyArea())
                .content(truncate(accompanyPost.getContent(), 36))
                .createdAt(DateTimeUtils.formatUTC(accompanyPost.getCreatedAt()))
                .profileImagePath(accompanyPost.getMemberEntity().getProfileImagePath())
                .build();
    }


}
