package io.app.stacktodobe.task.domain.model;

/**
 * 작업(Task) 상태
 */
public enum TaskStatus {
    PENDING,    // 진행 대기
    IN_PROGRESS,    // 진행중
    COMPLETED;  // 완료 percent_complete=100
}
