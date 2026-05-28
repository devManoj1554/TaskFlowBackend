package com.taskflow.user.dto;

import java.time.LocalDateTime;
import com.taskflow.user.entity.User;

public record UserDto(Long id, String username, String email, LocalDateTime createdAt) 
{
    public static UserDto from(User u) {
        return new UserDto(u.getId(), u.getUsername(), u.getEmail(), u.getCreatedAt());
    }
}
