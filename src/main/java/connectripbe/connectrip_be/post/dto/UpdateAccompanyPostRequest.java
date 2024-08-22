package connectripbe.connectrip_be.post.dto;

import connectripbe.connectrip_be.post.entity.enums.AccompanyArea;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UpdateAccompanyPostRequest(
        String title,
        String content,
        String accompanyArea,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}
