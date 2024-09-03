package connectripbe.connectrip_be.communitypost.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommunityPostRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String content;
}

