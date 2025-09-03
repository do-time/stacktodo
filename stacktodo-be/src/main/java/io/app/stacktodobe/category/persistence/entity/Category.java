package io.app.stacktodobe.category.persistence.entity;

import io.app.stacktodobe.common.entity.BaseEntity;
import io.app.stacktodobe.member.adapter.out.persistence.entity.MemberEntity;
import io.app.stacktodobe.workspace.persistence.entity.Workspace;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "categories")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(AuditingEntityListener.class)
public class Category extends BaseEntity {
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    // scope=='personal'일 때만 사용
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;
}