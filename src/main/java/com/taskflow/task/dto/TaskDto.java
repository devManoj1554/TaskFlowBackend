package com.taskflow.task.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.taskflow.task.enums.*;
import com.taskflow.task.entity.Task;

public record TaskDto(
    Long id,
    String title,
    String description,
    TaskStatus status,
    TaskPriority priority,
    LocalDate dueDate,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Long userId
) {
    public static TaskDto from(Task t) {
        return new TaskDto(
            t.getId(), t.getTitle(), t.getDescription(),
            t.getStatus(), t.getPriority(), t.getDueDate(),
            t.getCreatedAt(), t.getUpdatedAt(), t.getUser().getId()
        );
    }
}
