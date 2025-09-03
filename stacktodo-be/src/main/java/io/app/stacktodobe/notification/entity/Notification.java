package io.app.stacktodobe.notification.entity;

import io.app.stacktodobe.common.entity.BaseEntity;
import io.app.stacktodobe.member.adapter.out.persistence.entity.Member;
import io.app.stacktodobe.notification.enums.NotificationChannel;
import io.app.stacktodobe.notification.enums.NotificationEntityType;
import io.app.stacktodobe.notification.enums.NotificationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications",
        indexes = {
                @Index(name = "idx_notification_member", columnList = "member_id"),
                @Index(name = "idx_notification_schedule", columnList = "notifyAt"),
                @Index(name = "idx_notification_entity", columnList = "entityType,entityId")
        })
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(AuditingEntityListener.class)
public class Notification extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "entityType", nullable = false, length = 20)
    private NotificationEntityType entityType;

    @Column(name = "entityId", nullable = false)
    private Long entityId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 수신 대상

    @Column(nullable = false)
    @Builder.Default
    private boolean isRead = false;

    @Column(nullable = false)
    private LocalDateTime notifyAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private NotificationChannel channel = NotificationChannel.IN_APP;

    @Column(length = 50, nullable = false)
    private String eventType; // ex) fixed, countdown, due, shared, copied

    @Column(columnDefinition = "json")
    private String payload;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private NotificationStatus status = NotificationStatus.PENDING;

    private LocalDateTime sentAt;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
