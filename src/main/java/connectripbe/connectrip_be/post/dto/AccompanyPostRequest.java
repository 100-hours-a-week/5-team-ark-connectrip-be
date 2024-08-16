package connectripbe.connectrip_be.post.dto;

import connectripbe.connectrip_be.post.entity.AccompanyPost;
import connectripbe.connectrip_be.post.entity.enums.AccompanyArea;
import lombok.*;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AccompanyPostRequest {

    @NotNull(message = "제목을 입력해주세요.")
    private String title; // 게시글의 제목

    @NotNull(message = "내용을 입력해주세요.")
    private String content; // 게시글의 내용

    @NotNull(message = "동행 지역을 선택해주세요.")
    private AccompanyArea accompanyArea; // 드롭다운에서 선택된 동행 지역

    private LocalDate startDate; // 드롭다운에서 선택된 동행 시작 날짜

    private LocalDate endDate; // 드롭다운에서 선택된 동행 종료 날짜

    private String customUrl;

    // PostRequestDto를 Post 엔티티로 변환하는 메서드
    public AccompanyPost toEntity() {
        return AccompanyPost.builder()
                .title(this.title)
                .accompanyArea(this.accompanyArea) // Enum 값을 그대로 저장
                .content(this.content)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .customUrl(this.customUrl)
                .build();
    }

}
