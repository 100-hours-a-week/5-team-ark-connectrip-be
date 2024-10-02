package connectripbe.connectrip_be.accompany.comment.dto;

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

    @NotBlank(message = "내용은 필수입니다.")
    private String content;

}
