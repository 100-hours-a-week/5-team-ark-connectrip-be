package connectripbe.connectrip_be.notification.dto;

import connectripbe.connectrip_be.comment.entity.AccompanyCommentEntity;
import connectripbe.connectrip_be.notification.entity.NotificationEntity;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationCommentResponse {

    private Long userId;  // 댓글을 남긴 사용자 ID
    private String userNickname;  // 댓글을 남긴 사용자 닉네임
    private String userProfilePath;  // 댓글을 남긴 사용자 프로필 이미지 경로
    private Long postId;  // 댓글이 달린 게시물 ID
    private String content;  // 댓글 내용 (글자 제한 적용)
    private String notificationTime;  // 알림 생성 시간
    private boolean isRead;  // 읽음 여부

    // 기존 AccompanyCommentEntity를 사용하는 fromEntity
    public static NotificationCommentResponse fromEntity(AccompanyCommentEntity comment, String content) {
        return NotificationCommentResponse.builder()
                .userId(comment.getMemberEntity().getId())
                .userNickname(comment.getMemberEntity().getNickname())
                .userProfilePath(comment.getMemberEntity().getProfileImagePath())
                .postId(comment.getAccompanyPostEntity().getId())
                .content(content)  // 댓글 내용을 받아서 제한된 길이로 적용
                .notificationTime(formatToUTC(LocalDateTime.now()))  // 현재 시간을 UTC 형식으로 변환
                .isRead(false)  // 알림이 생성된 직후이므로 읽지 않은 상태로 처리
                .build();
    }

    // 새로운 NotificationEntity를 사용하는 fromEntity
    public static NotificationCommentResponse fromEntity(NotificationEntity notification) {
        return NotificationCommentResponse.builder()
                .userId(notification.getMember().getId())  // 사용자 ID
                .userNickname(notification.getMember().getNickname())  // 사용자 닉네임
                .userProfilePath(notification.getMember().getProfileImagePath())  // 프로필 이미지 경로
                .postId(notification.getAccompanyPostEntity().getId())  // 게시물 ID
                .content(notification.getMessage())  // 알림 메시지
                .notificationTime(formatToUTC(notification.getCreatedAt()))  // 알림 생성 시간
                .isRead(notification.getReadAt() != null)  // 읽음 여부
                .build();
    }


    // UTC 형식으로 변환하는 메서드
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
