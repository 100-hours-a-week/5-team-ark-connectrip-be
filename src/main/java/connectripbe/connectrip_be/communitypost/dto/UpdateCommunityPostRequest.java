package connectripbe.connectrip_be.communitypost.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCommunityPostRequest {

    private String title;
    private String content;
}
