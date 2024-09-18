package connectripbe.connectrip_be.member.entity;

import connectripbe.connectrip_be.Review.entity.AccompanyReviewEntity;
import connectripbe.connectrip_be.global.entity.BaseEntity;
import connectripbe.connectrip_be.member.entity.type.MemberLoginType;
import connectripbe.connectrip_be.member.entity.type.MemberRoleType;
import connectripbe.connectrip_be.post.entity.AccompanyPostEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "member")
public class MemberEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //아이디

    @Column(nullable = false, unique = true)
    private String email; //이메일

    @Column(name = "password")
    private String password; //비밀번호

    @Column(name = "nickname")
    private String nickname; // 닉네임

    @Column(name = "profile_image_path")
    private String profileImagePath; // 프로필 이미지

    @Column(name = "gender")
    private String gender; // 성별

    @Column(name = "description")
    private String description; // 자기소개

    @Column(name = "birth_date")
    private LocalDateTime birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "login_type", nullable = false, length = 10)
    private MemberLoginType loginType; // 로그인 타입 ex) 카카오, 이메일

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private MemberRoleType roleType; // 권한

    @Builder.Default
    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccompanyPostEntity> accompanyPostEntities = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "target", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccompanyReviewEntity> reviews = new ArrayList<>();

    @Builder.Default
    @Column(name = "accompany_count", nullable = false)
    private int accompanyCount = 0;

    public void profileUpdate(String nickname, String description) {
        this.nickname = nickname;
        this.description = description;
    }

}
