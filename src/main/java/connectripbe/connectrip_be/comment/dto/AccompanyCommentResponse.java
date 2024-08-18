package connectripbe.connectrip_be.comment.dto;

import connectripbe.connectrip_be.comment.entity.AccompanyCommentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccompanyCommentResponse {

    private Long id;  // 댓글 아이디
    private Long memberId;  // 사용자 아이디
    private Long accompanyPostId;  // 동행 아이디
    private String content;  // 내용
    private LocalDateTime createdDate;  // 생성 일자
    private LocalDateTime deletedDate;  // 삭제 일자 (null 가능)

    // 엔티티를 DTO로 변환하는 메서드
    public static AccompanyCommentResponse fromEntity(AccompanyCommentEntity comment) {
        return AccompanyCommentResponse.builder()
                .id(comment.getId())
                .memberId(comment.getMemberEntity().getId())
                .accompanyPostId(comment.getAccompanyPostEntity().getId())
                .content(comment.getContent())
                .createdDate(comment.getCreatedDate())
                .deletedDate(comment.getDeletedDate())
                .build();
    }
}
