package connectripbe.connectrip_be.community.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCommunityPostRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String content;
}
