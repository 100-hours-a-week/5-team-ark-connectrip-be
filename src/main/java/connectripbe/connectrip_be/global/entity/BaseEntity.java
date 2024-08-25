package connectripbe.connectrip_be.global.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    private LocalDateTime createdAt; // 생성일

    private LocalDateTime updatedAt; // 수정일

    private LocalDateTime deletedAt; // 삭제일

    @PrePersist
    public void prePersist() {
        // 현재 UTC 시간을 createdAt과 updatedAt에 설정
        LocalDateTime now = ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        // 엔티티가 업데이트될 때 UTC 시간으로 updatedAt 설정
        this.updatedAt = ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime();
    }

    public void deleteEntity() {
        // 현재 UTC 시간을 deletedAt에 설정
        this.deletedAt = ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime();
    }
}
