package connectripbe.connectrip_be.accompany.status.entity;

import connectripbe.connectrip_be.accompany.post.entity.AccompanyPostEntity;
import connectripbe.connectrip_be.global.entity.BaseEntity;
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
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "accompany_status")
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

    public void changeAccompanyStatus(AccompanyStatusEnum AccompanyStatusEnum) {
        this.accompanyStatusEnum = AccompanyStatusEnum;
    }
}
