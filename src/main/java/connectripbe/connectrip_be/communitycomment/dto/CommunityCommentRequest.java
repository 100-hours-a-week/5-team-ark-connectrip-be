package connectripbe.connectrip_be.communitycomment.dto;

import connectripbe.connectrip_be.communitycomment.entity.CommunityCommentEntity;
import connectripbe.connectrip_be.communitypost.entity.CommunityPostEntity;
import connectripbe.connectrip_be.member.entity.MemberEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityCommentRequest {

    @NotNull(message = "게시물 ID는 필수입니다.")
    private Long postId;

    @NotBlank(message = "내용은 필수입니다.")
    private String content;

    // DTO를 CommunityComment 엔티티로 변환하는 메서드
    public CommunityCommentEntity toEntity(CommunityPostEntity post, MemberEntity member) {
        return CommunityCommentEntity.builder()
                .communityPostEntity(post)
                .memberEntity(member)
                .content(this.content)
                .build();
    }
}
