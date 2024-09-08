package connectripbe.connectrip_be.member.dto;

import connectripbe.connectrip_be.auth.jwt.dto.TokenDto;

public record TokenAndHeaderInfoDto(
        TokenDto tokenDto,
        MemberHeaderInfoDto memberHeaderInfoDto
) {
}
