package io.app.stacktodobe.workspace.domain.model;

import io.app.stacktodobe.workspace.adapter.out.persistence.entity.WorkspaceMemberRole;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

public record Workspace(
        UUID workspaceId,
        String name,
        UUID ownerId,
        Set<WorkspaceMember> members
) {
    public static Workspace of(UUID workspaceId, String name, UUID ownerId) {
        return new Workspace(workspaceId, name, ownerId, new LinkedHashSet<>());
    }

    public static Workspace of(UUID workspaceId, String name, UUID ownerId, Set<WorkspaceMember> members) {
        return new Workspace(workspaceId, name, ownerId, members);
    }

    public void invite(UUID memberId, WorkspaceMemberRole role) {
        if (isMember(memberId)) throw new IllegalStateException("Already a member");

        members.add(WorkspaceMember.of(this.workspaceId, memberId, role));
    }

    public boolean isMember(UUID memberId) {
        return members.stream().anyMatch(m -> m.memberId().equals(memberId));
    }

    private static String requireNonBlank(String s, String name) {
        if (s == null || s.isBlank()) throw new IllegalArgumentException(name + " blank");
        return s;
    }
}
