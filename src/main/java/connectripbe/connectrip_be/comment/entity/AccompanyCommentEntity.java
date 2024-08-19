package connectripbe.connectrip_be.comment.entity;

import connectripbe.connectrip_be.global.entity.BaseEntity;
import connectripbe.connectrip_be.member.entity.MemberEntity;
import connectripbe.connectrip_be.post.entity.AccompanyPostEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "accompany_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class AccompanyCommentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity memberEntity;  // 사용자 아이디 (외래키)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accompany_post_id", nullable = false)
    private AccompanyPostEntity accompanyPostEntity;  // 동행 아이디 (외래키)

    @Column(nullable = false)
    private String content;  // 내용

    public AccompanyCommentEntity(MemberEntity memberEntity, AccompanyPostEntity accompanyPostEntity, String content) {
        this.memberEntity = memberEntity;
        this.accompanyPostEntity = accompanyPostEntity;
        this.content = content;

    }
    public void setContent(String content) {
        this.content = content;
    }


}
