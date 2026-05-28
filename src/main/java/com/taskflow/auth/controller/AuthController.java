package com.taskflow.auth.controller;

import com.taskflow.security.jwt.JwtService;
import com.taskflow.user.entity.User;
import com.taskflow.user.dto.UserDto;
import com.taskflow.user.repo.UserRepository;
import com.taskflow.common.exception.BadCredentialsException;
import com.taskflow.common.exception.BadRequestException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public record RegisterRequest(
        @jakarta.validation.constraints.NotBlank
		@jakarta.validation.constraints.Size(min=2,max=64)
		String username,
		
        @jakarta.validation.constraints.NotBlank 
		@jakarta.validation.constraints.Email 
		@jakarta.validation.constraints.Size(max=255) 
		String email,
		
        @jakarta.validation.constraints.NotBlank 
		@jakarta.validation.constraints.Size(min=6,max=72)
		String password
    ) {}

    public record LoginRequest(
        @jakarta.validation.constraints.NotBlank
		@jakarta.validation.constraints.Email 
		String email,
		
        @jakarta.validation.constraints.NotBlank 
		String password
    ) {}

    public record AuthResponse(String token, UserDto user) {}

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest req) 
	{
        if (userRepository.existsByEmail(req.email())) throw new BadCredentialsException("Email already registered");
		
        if (userRepository.existsByUsername(req.username())) throw new BadRequestException("Username already taken");
		
        User u = User.builder()
            .username(req.username())
            .email(req.email())
            .password(passwordEncoder.encode(req.password()))
            .build();
        u = userRepository.save(u);
        String token = jwtService.generate(u);
        return ResponseEntity.ok(new AuthResponse(token, UserDto.from(u)));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) 
	{
        //User u = userRepository.findByEmail(req.email()).orElseThrow(() -> new BadRequestException("Invalid email or password"));
        User u = userRepository.findByEmail(req.email()).orElseThrow(() -> new BadCredentialsException("Invalid email or password"));
       
	   if (!passwordEncoder.matches(req.password(), u.getPassword()))
            throw new BadCredentialsException("Invalid email or password");
		
        String token = jwtService.generate(u);
		
        return ResponseEntity.ok(new AuthResponse(token, UserDto.from(u)));
    }
}
