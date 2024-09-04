package connectripbe.connectrip_be.communitypost.dto;

import connectripbe.connectrip_be.communitypost.entity.CommunityPostEntity;
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
                .createdAt(post.getCreatedAt().toString())       // 필요 시 포맷 조정 가능
                .profileImagePath(post.getMemberEntity().getProfileImagePath()) // MemberEntity에서 가져옴
                .build();
    }
}
