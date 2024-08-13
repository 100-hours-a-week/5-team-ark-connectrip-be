package connectripbe.connectrip_be.global.util.aws.dto;

import lombok.Builder;


@Builder
public record S3ImageDto(
        Long id,
        String url,
        String fileName,
        Long size
) {




}
