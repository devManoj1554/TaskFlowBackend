package com.taskflow.task.repo;

import com.taskflow.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import com.taskflow.task.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> 
{
    List<Task> findAllByUserOrderByCreatedAtDesc(User user);
    Page<Task> findAllByUser(User user, Pageable pageable);
    Optional<Task> findByIdAndUser(Long id, User user);
}
