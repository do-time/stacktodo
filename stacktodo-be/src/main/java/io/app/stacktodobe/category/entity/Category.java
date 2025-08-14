package io.app.stacktodobe.category.entity;

import io.app.stacktodobe.category.enums.CategoryScope;
import io.app.stacktodobe.common.entity.BaseEntity;
import io.app.stacktodobe.member.persistence.entity.Member;
import io.app.stacktodobe.workspace.entity.Workspace;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "categories")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(AuditingEntityListener.class)
public class Category extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @Lob
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CategoryScope scope;

    // scope='WORKSPACE'일 때만 값 존재
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    // scope='PERSONAL'일 때만 값 존재
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 복사된 경우 원본 템플릿
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_category_id")
    private Category originalCategory;

    // 커뮤니티 공유 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shared_by_id")
    private Member sharedBy;

    private LocalDateTime sharedAt;
}