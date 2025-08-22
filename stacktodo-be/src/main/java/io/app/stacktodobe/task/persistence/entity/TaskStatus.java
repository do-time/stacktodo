package io.app.stacktodobe.task.persistence.entity;

/**
 * 작업(Task) 상태
 */
public enum TaskStatus {
    PENDING,    // 진행 대기
    COMPLETED,  // 완료 percent_complete=100
    ARCHIVED    // 보관됨
}
