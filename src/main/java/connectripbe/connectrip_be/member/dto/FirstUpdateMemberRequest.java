package connectripbe.connectrip_be.member.dto;


import java.time.LocalDateTime;

public record FirstUpdateMemberRequest(
        String nickname,
        LocalDateTime birthDate,
        String gender
) {
}
