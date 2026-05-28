package com.taskflow.user.service;

//import com.taskflow.common.exception.NotFoundException;
import com.taskflow.user.entity.User;
import org.springframework.stereotype.Service;
import java.util.List;
import com.taskflow.user.repo.UserRepository;
import com.taskflow.user.dto.UserDto;
import com.taskflow.user.dto.UpdateProfileRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

private final UserRepository userRepository;

    public UserDto me(User user) {
        return UserDto.from(user);
    }
	
    public UserDto update(User user, UpdateProfileRequest req) {
        user.setUsername(req.username());
        user.setEmail(req.email());
        return UserDto.from(userRepository.save(user));
    }
}