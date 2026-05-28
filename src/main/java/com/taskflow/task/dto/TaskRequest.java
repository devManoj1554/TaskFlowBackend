package com.taskflow.task.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import com.taskflow.task.enums.TaskStatus;
import com.taskflow.task.enums.TaskPriority;

public record TaskRequest(
    @NotBlank @Size(max = 160) String title,
    @Size(max = 5000) String description,
    TaskStatus status,
    TaskPriority priority,
    LocalDate dueDate
) {}
