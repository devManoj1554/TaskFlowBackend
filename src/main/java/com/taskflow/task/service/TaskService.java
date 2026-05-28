package com.taskflow.task.service;

import com.taskflow.common.exception.NotFoundException;
import com.taskflow.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import com.taskflow.task.repo.TaskRepository;
import com.taskflow.task.entity.Task;
import com.taskflow.user.entity.User;
import com.taskflow.task.dto.TaskRequest;
import com.taskflow.task.enums.*;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository repo;

    @Transactional(readOnly = true)
    public List<Task> listAll(User user) 
	{ 
		return repo.findAllByUserOrderByCreatedAtDesc(user); 
	}

    @Transactional(readOnly = true)
    public Page<Task> listPaged(User user, Pageable pageable) 
	{ 
		return repo.findAllByUser(user, pageable); 
	}

    @Transactional
    public Task create(User user, TaskRequest r) 
	{
        Task t = Task.builder()
            .user(user)
            .title(r.title())
            .description(r.description())
            .status(r.status() == null ? TaskStatus.TODO : r.status())
            .priority(r.priority() == null ? TaskPriority.MEDIUM : r.priority())
            .dueDate(r.dueDate())
            .build();
        return repo.save(t);
    }

    @Transactional
    public Task update(User user, Long id, TaskRequest r) 
	{
        Task t = repo.findByIdAndUser(id, user).orElseThrow(() -> new NotFoundException("Task not found"));
        if (r.title() != null) t.setTitle(r.title());
        t.setDescription(r.description());
        if (r.status() != null) t.setStatus(r.status());
        if (r.priority() != null) t.setPriority(r.priority());
        t.setDueDate(r.dueDate());
        return t;
    }

    @Transactional
    public void delete(User user, Long id) 
	{
        Task t = repo.findByIdAndUser(id, user).orElseThrow(() -> new NotFoundException("Task not found"));
        repo.delete(t);
    }
}
