package io.app.stacktodobe.category.application.port.in;

import io.app.stacktodobe.category.adapter.in.web.dto.CategoryView;

import java.util.List;
import java.util.UUID;

public interface CategoryQueryUseCase {
    CategoryView getPersonalCategory(UUID categoryId , UUID memberId);
    CategoryView getWorkspaceCategory(UUID categoryId , UUID workspaceId, UUID memberId);
    CategoryView getCommunityCategory(UUID categoryId);
    List<CategoryView> getPersonalCategoryList(UUID memberId);
    List<CategoryView> getWorkspaceCategoryList(UUID workspaceId, UUID memberId);
    List<CategoryView> getCommunityCategoryList();
}
