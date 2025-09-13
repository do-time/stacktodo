package io.app.stacktodobe.category.domain.model;

import io.app.stacktodobe.common.utils.SlugUtils;

import java.util.UUID;


public class Category {

    private final UUID categoryId;     // DB PK 또는 publicId
    private String name;               // 카테고리 이름
    private String description;        // 설명
    private String slug;               // 검색/공유용 slug
    private final CategoryScope scope; // personal/workspace/community
    private final UUID workspaceId;    // scope == workspace 일 때 사용
    private final UUID memberId;       // scope == personal 일 때 사용

    // ====== 생성자 ======
    private Category(UUID categoryId, String name, String description,
                     String slug, CategoryScope scope, UUID workspaceId, UUID memberId) {
        slug = slug == null ? SlugUtils.slugify(name) : slug;

        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.slug = slug;
        this.scope = scope == null ? CategoryScope.PERSONAL : scope;
        this.workspaceId = workspaceId;
        this.memberId = memberId;
    }

    // ====== 정적 팩토리 메서드 ======
    public static Category ofPersonal(UUID memberId, String name, String description) {
        return new Category(UUID.randomUUID(), name, description, null, CategoryScope.PERSONAL, null, memberId);
    }

    public static Category ofWorkspace(UUID workspaceId, String name, String description) {
        return new Category(UUID.randomUUID(), name, description, null, CategoryScope.WORKSPACE, workspaceId, null);
    }

    public static Category ofCommunity(String name, String description) {
        return new Category(UUID.randomUUID(), name, description, null, CategoryScope.COMMUNITY, null, null);
    }

    public static Category reconstruct(UUID categoryId, String name, String description, String slug,
                                       CategoryScope scope, UUID workspaceId, UUID memberId) {
        return new Category(categoryId, name, description, slug, scope, workspaceId, memberId);
    }

    // ====== Getter ======
    public UUID getCategoryId() { return categoryId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getSlug() { return slug; }
    public CategoryScope getScope() { return scope; }
    public UUID getWorkspaceId() { return workspaceId; }
    public UUID getMemberId() { return memberId; }
}