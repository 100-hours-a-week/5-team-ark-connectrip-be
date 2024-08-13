package connectripbe.connectrip_be.member.entity;

import connectripbe.connectrip_be.global.entity.BaseEntity;
import connectripbe.connectrip_be.member.entity.type.MemberLoginType;
import connectripbe.connectrip_be.member.entity.type.MemberRoleType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Table;
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
public class Member extends BaseEntity {

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

      @Column(name = "age")
      private String age; // 나이

      @Enumerated(EnumType.STRING)
      @Column(name= "login_type", nullable = false, length = 10)
      private MemberLoginType loginType; // 로그인 타입 ex) 카카오, 이메일

      @Enumerated(EnumType.STRING)
      @Column(nullable = false, length = 10)
      private MemberRoleType roleType; // 권한

//      @Builder.Default
//      @OneToMany(mappedBy = "member")
//      private List<Post> posts = new ArrayList<>();
//
//      @Builder.Default
//      @OneToMany(mappedBy = "member")
//      private List<Meeting> myMeetingList = new ArrayList<>();
//
//      public void addPost(Post post) {
//            this.posts.add(post);
//            post.assignMember(this);
//      }
//
//      public void removePost(Post post) {
//            this.posts.remove(post);
//            post.assignMember(null);
//      }


}
