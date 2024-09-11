package connectripbe.connectrip_be.comment.dto;

import connectripbe.connectrip_be.comment.entity.AccompanyCommentEntity;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccompanyCommentResponse {

    private Long id;  // 댓글 아이디
    private Long memberId;  // 사용자 아이디
    private Long accompanyPostId;  // 동행 아이디
    private String memberNickname;  // 사용자 닉네임
    private String memberProfileImage;  // 사용자 프로필 이미지
    private String content;  // 내용
    private String createdAt;  // 생성 일자
    private String updatedAt;
    private String deletedAt;  // 삭제 일자 (null 가능)


    // 엔티티를 DTO로 변환하는 메서드
    public static AccompanyCommentResponse fromEntity(AccompanyCommentEntity comment) {
        return AccompanyCommentResponse.builder()
                .id(comment.getId())
                .memberId(comment.getMemberEntity().getId())
                .accompanyPostId(comment.getAccompanyPostEntity().getId())
                .memberNickname(comment.getMemberEntity().getNickname())
                .memberProfileImage(comment.getMemberEntity().getProfileImagePath())
                .content(comment.getContent())
                .createdAt(formatToUTC(comment.getCreatedAt()))
                .updatedAt(formatToUTC(comment.getUpdatedAt()))
                .deletedAt(formatToUTC(comment.getDeletedAt()))
                .build();
    }

    // UTC 형식으로 변환하는 메서드 추가
    private static final DateTimeFormatter UTC_FORMATTER = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static String formatToUTC(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.atZone(ZoneId.systemDefault())
                .format(UTC_FORMATTER);
    }
}
