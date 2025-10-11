package io.app.stacktodobe.category.adapter.in.web.controller;

import io.app.stacktodobe.category.adapter.in.web.dto.CategoryView;
import io.app.stacktodobe.category.application.port.in.CategoryQueryUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/categories")
public class CategoryQueryController {
    private final CategoryQueryUseCase categoryQueryUseCase;

    @GetMapping("/personal/{categoryId}")
    public ResponseEntity<CategoryView> getPersonalCategory(@PathVariable UUID categoryId, Principal principal){
        UUID memberId = UUID.fromString(principal.getName());

        log.info("GET personal category: memberId={}, categoryId={}", memberId, categoryId);
        var view = categoryQueryUseCase.getPersonalCategory(categoryId, memberId);

        return ResponseEntity.ok(view);
    }

    @GetMapping("/workspace/{workspaceId}/{categoryId}")
    public ResponseEntity<CategoryView> getWorkspaceCategory(@PathVariable UUID categoryId, @PathVariable UUID workspaceId, Principal principal){
        UUID memberId = UUID.fromString(principal.getName());

        log.info("GET workspace category: memberId={}, workspaceId={}, categoryId={}", memberId, workspaceId, categoryId);
        var view = categoryQueryUseCase.getWorkspaceCategory(categoryId, workspaceId, memberId);

        return ResponseEntity.ok(view);
    }

    @GetMapping("/community/{categoryId}")
    public ResponseEntity<CategoryView> getCommunityCategory(@PathVariable UUID categoryId, Principal principal){
        UUID memberId = UUID.fromString(principal.getName());

        log.info("GET community category: memberId={}, categoryId={}", memberId, categoryId);
        var view = categoryQueryUseCase.getCommunityCategory(categoryId);

        return ResponseEntity.ok(view);
    }

    @GetMapping("/personal")
    public ResponseEntity<List<CategoryView>> getPersonalCategoryList(Principal principal){
        UUID memberId = UUID.fromString(principal.getName());

        log.info("GET personal category list: memberId={}", memberId);
        var view = categoryQueryUseCase.getPersonalCategoryList(memberId);

        return ResponseEntity.ok(view);
    }

    @GetMapping("/workspace/{workspaceId}")
    public ResponseEntity<List<CategoryView>> getWorkspaceCategoryList(@PathVariable UUID workspaceId, Principal principal){
        UUID memberId = UUID.fromString(principal.getName());

        log.info("GET workspace category list: memberId={}, workspaceId={}", memberId, workspaceId);
        var view = categoryQueryUseCase.getWorkspaceCategoryList(workspaceId, memberId);

        return ResponseEntity.ok(view);
    }

    @GetMapping("/community")
    public ResponseEntity<List<CategoryView>> getCommunityCategoryList(Principal principal){
        UUID memberId = UUID.fromString(principal.getName());

        log.info("GET community category list: memberId={}", memberId);
        var view = categoryQueryUseCase.getCommunityCategoryList();

        return ResponseEntity.ok(view);
    }
}
