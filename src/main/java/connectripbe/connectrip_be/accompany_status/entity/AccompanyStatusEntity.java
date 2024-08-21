package connectripbe.connectrip_be.accompany_status.entity;

import connectripbe.connectrip_be.global.entity.BaseEntity;
import connectripbe.connectrip_be.post.entity.AccompanyPostEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AccompanyStatusEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accompany_post_id", nullable = false)
    private AccompanyPostEntity accompanyPostEntity;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccompanyStatusEnum accompanyStatusEnum;

    public AccompanyStatusEntity(AccompanyPostEntity accompanyPostEntity, AccompanyStatusEnum accompanyStatusEnum) {
        this.accompanyPostEntity = accompanyPostEntity;
        this.accompanyStatusEnum = accompanyStatusEnum;
    }
}
