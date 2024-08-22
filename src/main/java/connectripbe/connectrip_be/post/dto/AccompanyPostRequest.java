package connectripbe.connectrip_be.post.dto;


import java.time.LocalDateTime;

public record AccompanyPostRequest(
        String title,
        String content,
        String accompanyArea,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}
