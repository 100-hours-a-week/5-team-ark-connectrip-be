package connectripbe.connectrip_be.post.dto;

import connectripbe.connectrip_be.post.entity.AccompanyPostEntity;
import connectripbe.connectrip_be.post.entity.enums.AccompanyArea;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccompanyPostResponse {

    private Long accompanyPostId; // 게시글의 고유 ID
    private Long memberId;
    private String title; // 게시글의 제목
    private AccompanyArea accompanyArea; // 동행 지역
    private String content; // 게시글의 내용
    private LocalDate startDate; // 동행 시작 날짜
    private LocalDate endDate; // 동행 종료 날짜
    private LocalDateTime createdAt; // 게시글 생성일
    private String customUrl;

    // Post 엔티티를 PostResponseDto로 변환하는 메서드
    public static AccompanyPostResponse fromEntity(AccompanyPostEntity accompanyPostEntity) {
        return AccompanyPostResponse.builder()
                .accompanyPostId(accompanyPostEntity.getId())
                .memberId(accompanyPostEntity.getMember().getId())
                .title(accompanyPostEntity.getTitle())
                .accompanyArea(accompanyPostEntity.getAccompanyArea()) // Enum 값을 그대로 할당
                .content(accompanyPostEntity.getContent())
                .startDate(accompanyPostEntity.getStartDate())
                .endDate(accompanyPostEntity.getEndDate())
                .createdAt(accompanyPostEntity.getCreatedAt())
                .customUrl(accompanyPostEntity.getCustomUrl())
                .build();
    }
}
