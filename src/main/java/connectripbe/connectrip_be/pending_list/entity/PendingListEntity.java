package connectripbe.connectrip_be.pending_list.entity;

import connectripbe.connectrip_be.global.entity.BaseEntity;
import connectripbe.connectrip_be.member.entity.MemberEntity;
import connectripbe.connectrip_be.pending_list.entity.type.PendingStatus;
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

@Entity(name = "pending_list")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PendingListEntity extends BaseEntity {

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
    @Column(name = "status", nullable = false)
    private PendingStatus status;


    public void addPendingList(MemberEntity member, AccompanyPostEntity accompanyPost) {
        this.member = member;
        this.accompanyPost = accompanyPost;
        this.status = PendingStatus.PENDING;
    }

    public void acceptStatus() {
        this.status = PendingStatus.ACCEPT;
    }

    public void rejectStatus() {
        this.status = PendingStatus.REJECT;
    }

}
