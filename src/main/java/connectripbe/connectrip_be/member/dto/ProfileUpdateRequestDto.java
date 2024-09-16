package connectripbe.connectrip_be.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUpdateRequestDto {
    private String nickname;
    private String description;
}
