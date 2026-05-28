package com.taskflow.user.controller;

import com.taskflow.security.annotation.AuthenticatedUser;
import com.taskflow.user.service.UserService;
import com.taskflow.user.entity.User;
import com.taskflow.user.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService uservice;

    @GetMapping("/me")
    public UserDto me(@AuthenticatedUser User user) {
        return uservice.me(user);
    }

    @PutMapping("/me")
    public UserDto update(@AuthenticatedUser User user, @Valid @RequestBody UpdateProfileRequest req) {
        return uservice.update(user,req);
    }
}
