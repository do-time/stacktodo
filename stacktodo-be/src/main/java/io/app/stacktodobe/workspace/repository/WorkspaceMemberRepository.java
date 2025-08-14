package io.app.stacktodobe.workspace.repository;

import io.app.stacktodobe.workspace.entity.WorkspaceMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkspaceMemberRepository extends JpaRepository<WorkspaceMember, Long> {
//    Optional<WorkspaceMember> findByWorkspaceIdAndMemberId(Long workspaceId, Long memberId);
//    boolean existsByWorkspaceIdAndMemberId(Long workspaceId, Long memberId);
}
