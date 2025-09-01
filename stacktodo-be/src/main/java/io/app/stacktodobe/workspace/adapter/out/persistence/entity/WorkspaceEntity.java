package io.app.stacktodobe.workspace.adapter.out.persistence.entity;

import io.app.stacktodobe.common.entity.BaseEntity;
import io.app.stacktodobe.member.persistence.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Entity
@Table(name = "workspace",
        uniqueConstraints = @UniqueConstraint(columnNames = {"owner_id", "name"}))
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Builder(access = AccessLevel.PRIVATE)
public class WorkspaceEntity extends BaseEntity{
    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "workspace_id", unique = true, nullable = false)
    private UUID workspaceId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private Member owner;

    public static WorkspaceEntity createWorkspace(String name, UUID workspaceId, Member owner) {
        return WorkspaceEntity.builder()
                .name(name)
                .workspaceId(workspaceId)
                .owner(owner)
                .build();
    }
}