package io.app.stacktodobe.category.adapter.out.persistence.entity;

import io.app.stacktodobe.category.domain.model.CategoryScope;
import io.app.stacktodobe.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Entity
@Table(name = "categories")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(AuditingEntityListener.class)
public class CategoryEntity extends BaseEntity {
    @Column(name = "category_id", unique = true, nullable = false)
    private UUID categoryId;

    // 카테고리 표시명(예: 루틴, 여행계획, 아침 루틴 등)
    @Column(nullable = false, length = 100)
    private String name;

    private String description;

    // 선택: 검색/공유를 위한 슬러그
    @Column(length = 120, unique = false)
    private String slug;

    // 공개 범위(개인/워크스페이스/커뮤니티)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CategoryScope scope; // personal/workspace/community

    // scope=='workspace'일 때만 사용
    @JoinColumn(name = "workspace_id")
    private UUID workspaceId;

    // scope=='personal'일 때만 사용
    @JoinColumn(name = "member_id")
    private UUID memberId;
}