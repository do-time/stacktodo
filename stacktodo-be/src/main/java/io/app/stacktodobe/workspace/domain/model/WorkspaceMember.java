package io.app.stacktodobe.workspace.domain.model;

import io.app.stacktodobe.workspace.adapter.out.persistence.entity.WorkspaceMemberRole;
import lombok.Builder;

import java.util.UUID;

@Builder
public record WorkspaceMember (
    UUID workspaceId,
    UUID memberId,
    WorkspaceMemberRole role
){
        public static WorkspaceMember of(UUID workspaceId, UUID memberId, WorkspaceMemberRole role) {
            return WorkspaceMember.builder()
                    .workspaceId(workspaceId)
                    .memberId(memberId)
                    .role(role)
                    .build();
        }
}
