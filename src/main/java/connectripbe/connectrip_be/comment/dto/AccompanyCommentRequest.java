package connectripbe.connectrip_be.comment.dto;

import connectripbe.connectrip_be.comment.entity.AccompanyCommentEntity;
import connectripbe.connectrip_be.member.entity.MemberEntity;
import connectripbe.connectrip_be.post.entity.AccompanyPostEntity;
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
public class AccompanyCommentRequest {

    @NotNull(message = "게시물 ID는 필수입니다.")
    private Long postId;

    @NotNull(message = "사용자 ID는 필수입니다.")
    private Long memberId;

    @NotBlank(message = "내용은 필수입니다.")
    private String content;

    // DTO를 AccompanyComment 엔티티로 변환하는 메서드
    public AccompanyCommentEntity toEntity(AccompanyPostEntity post, MemberEntity member) {
        return AccompanyCommentEntity.builder()
                .accompanyPostEntity(post)
                .memberEntity(member)
                .content(this.content)
                .build();
    }
}
