package connectripbe.connectrip_be.post.entity;

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
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "accompany_post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class AccompanyPostEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member__id", nullable = false)
    private MemberEntity memberEntity;

    @Column(nullable = false)
    private String title;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String accompanyArea;

    private String customUrl;

    // fixme-noah: urlQrPath 임시 보류
    private String urlQrPath;

    @Column(nullable = false)
    private String content;

    // fixme-eric 동행 요청 상태 임시보류

    public AccompanyPostEntity(MemberEntity memberEntity, String title, LocalDateTime startDate, LocalDateTime endDate,
                               String accompanyArea, String customUrl, String urlQrPath, String content) {
        this.memberEntity = memberEntity;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.accompanyArea = accompanyArea;
        this.customUrl = customUrl;
        this.urlQrPath = urlQrPath;
        this.content = content;
    }

    public void updateAccompanyPost(String title, LocalDateTime startDate, LocalDateTime endDate, String accompanyArea,
                                    String content, String customUrl) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.accompanyArea = accompanyArea;
        this.content = content;
        this.customUrl = customUrl;
    }

}
