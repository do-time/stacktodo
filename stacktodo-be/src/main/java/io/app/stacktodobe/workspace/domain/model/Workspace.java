package io.app.stacktodobe.workspace.domain.model;

import io.app.stacktodobe.workspace.adapter.out.persistence.entity.WorkspaceMemberRole;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Builder
public record  Workspace(
        UUID workspaceId,
        String name,
        UUID ownerId,
        Set<WorkspaceMember> members
){
    /** 정적 팩토리: 빌더를 사용해 생성 */
    public static Workspace of(UUID workspaceId, String name, UUID ownerId) {
        return Workspace.builder()
                .workspaceId(workspaceId)
                .name(name)
                .ownerId(ownerId)
                .members(new LinkedHashSet<>())
                .build();
    }

    public static Workspace of(UUID workspaceId, String name, UUID ownerId, Set<WorkspaceMember> members) {
        return Workspace.builder()
                .workspaceId(workspaceId)
                .name(name)
                .ownerId(ownerId)
                .members(members)
                .build();
    }

    public void invite(UUID memberId, WorkspaceMemberRole role){
        if (isMember(memberId)) throw new IllegalStateException("Already a member");

        members.add(WorkspaceMember.of(this.workspaceId, memberId, role));
    }

    public boolean isMember(UUID memberId){
        return members.stream().anyMatch(m -> m.memberId().equals(memberId));
    }

    private static String requireNonBlank(String s, String name){
        if (s == null || s.isBlank()) throw new IllegalArgumentException(name+" blank");
        return s;
    }
}
