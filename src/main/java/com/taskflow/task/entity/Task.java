package com.taskflow.task.entity;

import com.taskflow.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.taskflow.task.enums.TaskPriority;
import com.taskflow.task.enums.TaskStatus;

@Entity
@Table(name = "tasks", indexes = {
    @Index(name = "idx_tasks_user", columnList = "user_id"),
    @Index(name = "idx_tasks_status", columnList = "status")
})
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class Task 
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 160)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TaskPriority priority;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
        if (status == null) status = TaskStatus.TODO;
        if (priority == null) priority = TaskPriority.MEDIUM;
    }

    @PreUpdate
    void onUpdate() { this.updatedAt = LocalDateTime.now(); }
}
