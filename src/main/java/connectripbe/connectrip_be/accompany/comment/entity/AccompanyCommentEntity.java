package connectripbe.connectrip_be.accompany.comment.entity;

import connectripbe.connectrip_be.accompany.post.entity.AccompanyPostEntity;
import connectripbe.connectrip_be.global.entity.BaseEntity;
import connectripbe.connectrip_be.member.entity.MemberEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "accompany_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class AccompanyCommentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity memberEntity;  // 사용자 아이디 (외래키)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accompany_post_id", nullable = false)
    private AccompanyPostEntity accompanyPostEntity;  // 동행 아이디 (외래키)

    @Setter
    @Column(nullable = false, length = 256)
    private String content;  // 내용

    // 기존 생성자 제거 (빌더 패턴으로 대체됨)
}
