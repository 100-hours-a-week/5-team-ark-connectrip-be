package connectripbe.connectrip_be.notification.entity;

import connectripbe.connectrip_be.global.entity.BaseEntity;
import connectripbe.connectrip_be.member.entity.MemberEntity;
import connectripbe.connectrip_be.post.entity.AccompanyPostEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notifications")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;  // 알림을 받을 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accompany_post_id", nullable = false)
    private AccompanyPostEntity accompanyPostEntity;

    @Column(nullable = false, length = 256)
    private String message;  // 알림 내용

    @Column(name = "read_at")
    private LocalDateTime readAt;  // 읽은 시간

    // 읽음 처리 메서드
    public void markAsRead(LocalDateTime readAt) {
        this.readAt = readAt;
    }
}