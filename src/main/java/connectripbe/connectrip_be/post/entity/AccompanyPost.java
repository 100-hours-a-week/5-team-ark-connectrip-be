package connectripbe.connectrip_be.post.entity;

import connectripbe.connectrip_be.global.entity.BaseEntity;
import connectripbe.connectrip_be.member.entity.Member;
import connectripbe.connectrip_be.post.entity.enums.AccompanyArea;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "accompany_post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AccompanyPost extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String title;

    private LocalDate startDate;

    private LocalDate endDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccompanyArea accompanyArea;

    // fixme-noah: customUrl 임시 보류
    private String customUrl;

    // fixme-noah: urlQrPath 임시 보류
    private String urlQrPath;

    @Column(nullable = false)
    private String content;

    public AccompanyPost(Member member, String title, LocalDate startDate, LocalDate endDate, AccompanyArea accompanyArea, String customUrl, String urlQrPath, String content) {
        this.member = member;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.accompanyArea = accompanyArea;
        this.customUrl = customUrl;
        this.urlQrPath = urlQrPath;
        this.content = content;
    }

    public void updateAccompanyPost(String title, LocalDate startDate, LocalDate endDate, AccompanyArea accompanyArea, String content) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.accompanyArea = accompanyArea;
        this.content = content;
    }
}
