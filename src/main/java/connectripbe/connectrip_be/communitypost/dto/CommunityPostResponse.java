package connectripbe.connectrip_be.communitypost.dto;

import connectripbe.connectrip_be.communitypost.entity.CommunityPostEntity;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CommunityPostResponse {

    private Long id;                // 게시글의 ID
    private Long memberId;           // 작성자의 ID
    private String nickname;         // 작성자의 닉네임
    private String title;            // 게시글 제목
    private String content;          // 게시글 내용
    private String createdAt;        // 생성 일시
    private String profileImagePath; // 작성자의 프로필 이미지 경로 (nullable)

    public static CommunityPostResponse fromEntity(CommunityPostEntity post) {
        return CommunityPostResponse.builder()
                .id(post.getId())
                .memberId(post.getMemberEntity().getId())
                .nickname(post.getMemberEntity().getNickname())  // MemberEntity에서 가져옴
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(formatToUTC(post.getCreatedAt()))
                .profileImagePath(post.getMemberEntity().getProfileImagePath()) // MemberEntity에서 가져옴
                .build();
    }

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
