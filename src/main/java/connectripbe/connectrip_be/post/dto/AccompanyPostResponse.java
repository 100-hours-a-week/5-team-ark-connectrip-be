package connectripbe.connectrip_be.post.dto;

import connectripbe.connectrip_be.post.entity.enums.AccompanyArea;

import java.time.LocalDate;

public record AccompanyPostResponse(
        long id,
        long memberId,
        String nickname,
        String profileImagePath,
        String title,
        LocalDate startDate,
        LocalDate endDate,
        AccompanyArea accompanyArea,
        String customUrl,
        String urlQrPath,
        String content
) {
}
