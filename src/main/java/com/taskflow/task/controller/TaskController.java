package com.taskflow.task.controller;

import com.taskflow.security.annotation.AuthenticatedUser;
import com.taskflow.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.taskflow.task.dto.TaskDto;
import com.taskflow.task.dto.TaskRequest;
import com.taskflow.task.service.TaskService;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService service;

    @GetMapping
    public List<TaskDto> list(@AuthenticatedUser User user) {
        return service.listAll(user).stream().map(TaskDto::from).toList();
    }

    @GetMapping("/page")
    public Page<TaskDto> paged(@AuthenticatedUser User user, Pageable pageable) {
        return service.listPaged(user, pageable).map(TaskDto::from);
    }

    @PostMapping
    public ResponseEntity<TaskDto> create(@AuthenticatedUser User user, @Valid @RequestBody TaskRequest r) {
        return ResponseEntity.ok(TaskDto.from(service.create(user, r)));
    }

    @PutMapping("/{id}")
    public TaskDto update(@AuthenticatedUser User user, @PathVariable Long id, @Valid @RequestBody TaskRequest r) {
        return TaskDto.from(service.update(user, id, r));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticatedUser User user, @PathVariable Long id) {
        service.delete(user, id);
        return ResponseEntity.noContent().build();
    }
}
