package connectripbe.connectrip_be.accompany.post.dto;

import java.time.LocalDateTime;

public record CreateAccompanyPostRequest(
        String title,
        String content,
        String accompanyArea,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}
