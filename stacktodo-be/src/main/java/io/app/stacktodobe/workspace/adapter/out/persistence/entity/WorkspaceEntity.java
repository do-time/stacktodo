package io.app.stacktodobe.workspace.adapter.out.persistence.entity;

import io.app.stacktodobe.common.entity.BaseEntity;
import io.app.stacktodobe.member.adapter.out.persistence.entity.MemberEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Entity
@Table(name = "workspace",
        uniqueConstraints = @UniqueConstraint(columnNames = {"owner_id", "name"}))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class WorkspaceEntity extends BaseEntity{
    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "workspace_id", unique = true, nullable = false)
    private UUID workspaceId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private MemberEntity owner;

    public static WorkspaceEntity createWorkspace(String name, UUID workspaceId, MemberEntity owner) {
        return WorkspaceEntity.builder()
                .name(name)
                .workspaceId(workspaceId)
                .owner(owner)
                .build();
    }
}