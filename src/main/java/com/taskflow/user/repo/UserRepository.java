package com.taskflow.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.taskflow.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
