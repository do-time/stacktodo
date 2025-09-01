package io.app.stacktodobe.workspace.adapter.out.persistence.entity;

import io.app.stacktodobe.common.entity.BaseEntity;
import io.app.stacktodobe.member.persistence.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(
        name = "workspace_members",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_workspace_member",
                columnNames = {"workspace_id", "member_id"}
        )
)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Builder(access = AccessLevel.PRIVATE)
public class WorkspaceMemberEntity extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private WorkspaceEntity workspaceEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private WorkspaceMemberRole role = WorkspaceMemberRole.MEMBER;

    public static WorkspaceMemberEntity createWorkspaceMember(WorkspaceEntity workspaceEntity, Member member, WorkspaceMemberRole role){
        return WorkspaceMemberEntity.builder()
                .workspaceEntity(workspaceEntity)
                .member(member)
                .role(role)
                .build();
    }
}