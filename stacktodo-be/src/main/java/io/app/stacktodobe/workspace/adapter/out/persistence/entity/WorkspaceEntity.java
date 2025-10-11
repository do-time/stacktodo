package io.app.stacktodobe.workspace.adapter.out.persistence.entity;

import io.app.stacktodobe.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.*;

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

    @Column(name = "owner_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID ownerId;

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.PERSIST, orphanRemoval = true)
    @Builder.Default
    private Set<WorkspaceMemberEntity> members = new HashSet<>();

    public void addMember(UUID memberId, WorkspaceMemberRole role) {
        this.members.add(WorkspaceMemberEntity.of(this, memberId, role));
    }

    public static WorkspaceEntity of(String name, UUID workspaceId, UUID ownerId) {
        return WorkspaceEntity.builder()
                .name(name)
                .workspaceId(workspaceId)
                .ownerId(ownerId)
                .build();
    }
}