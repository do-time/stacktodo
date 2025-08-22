package io.app.stacktodobe.tasklist.persistence.entity;

import io.app.stacktodobe.category.persistence.entity.Category;
import io.app.stacktodobe.common.entity.BaseEntity;
import io.app.stacktodobe.member.persistence.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "tasks")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Builder(access = AccessLevel.PRIVATE)
public class TaskList extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Lob
    private String description;

    /* 방식 추가 검토
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Scope scope; // personal/workspace/community
     */

    /* 방식 추가 검토
    // 소유 범위
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private Workspace workspace; // scope==workspace일 때 사용
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // scope==personal일 때 사용

    // 템플릿 복사 원본
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_id")
    private TaskList originalId;

    // 커뮤니티 공유 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shared_by_id")
    private Member sharedBy;

    @Column(nullable = false)
    @Builder.Default
    private boolean isRoutine = false;

    @Column(name = "shared_at")
    private java.time.LocalDateTime sharedAt;


}
