package connectripbe.connectrip_be.accompany_member.entity;

import connectripbe.connectrip_be.accompany_member.entity.type.AccompanyMemberStatus;
import connectripbe.connectrip_be.global.entity.BaseEntity;
import connectripbe.connectrip_be.member.entity.MemberEntity;
import connectripbe.connectrip_be.post.entity.AccompanyPostEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "accompany_member")
public class AccompanyMemberEntity extends BaseEntity {

      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;

      @ManyToOne(fetch = FetchType.LAZY)
      @JoinColumn(name = "member_id")
      private MemberEntity member;

      @ManyToOne(fetch = FetchType.LAZY)
      @JoinColumn(name = "accompany_post_id")
      private AccompanyPostEntity accompanyPost;

      @Enumerated(EnumType.STRING)
      @Builder.Default
      @Column(name = "status",nullable = false)
      private AccompanyMemberStatus status = AccompanyMemberStatus.ACTIVE;


      /**
       * 채팅방에서 회원이 나갈 때 사용
       */
      public void exitAccompany() {
            this.status = AccompanyMemberStatus.EXIT;
      }



}
