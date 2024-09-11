package connectripbe.connectrip_be.post.dto;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record UpdateAccompanyPostRequest(
        String title,
        String content,
        String accompanyArea,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}
