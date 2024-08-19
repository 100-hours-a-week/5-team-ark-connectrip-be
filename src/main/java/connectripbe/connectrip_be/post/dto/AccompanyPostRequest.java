package connectripbe.connectrip_be.post.dto;

import connectripbe.connectrip_be.post.entity.enums.AccompanyArea;

import java.time.LocalDate;

public record AccompanyPostRequest(
        String title,
        String content,
        AccompanyArea accompanyArea,
        LocalDate startDate,
        LocalDate endDate
) {
}
