package io.app.stacktodobe.workspace.domain.model;

import lombok.Builder;

import java.util.UUID;

@Builder
public record  Workspace(
        UUID workspaceId,
        String name,
        UUID ownerPublicId
){
    /** 정적 팩토리: 빌더를 사용해 생성 */
    public static Workspace of(UUID workspaceId, String name, UUID ownerPublicId) {
        return Workspace.builder()
                .workspaceId(workspaceId)
                .name(name)
                .ownerPublicId(ownerPublicId)
                .build();
    }
}
