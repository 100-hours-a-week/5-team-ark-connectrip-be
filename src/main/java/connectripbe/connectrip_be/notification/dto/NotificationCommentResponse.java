package connectripbe.connectrip_be.notification.dto;

import connectripbe.connectrip_be.comment.entity.AccompanyCommentEntity;
import java.time.LocalDateTime;
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
    private LocalDateTime notificationTime;  // 알림 생성 시간
    private boolean isRead;  // 읽음 여부


    public static NotificationCommentResponse fromEntity(AccompanyCommentEntity comment, String content) {
        return NotificationCommentResponse.builder()
                .userId(comment.getMemberEntity().getId())
                .userNickname(comment.getMemberEntity().getNickname())
                .userProfilePath(comment.getMemberEntity().getProfileImagePath())
                .postId(comment.getAccompanyPostEntity().getId())
                .content(content)
                .notificationTime(LocalDateTime.now())
                .isRead(false)
                .build();
    }
}
