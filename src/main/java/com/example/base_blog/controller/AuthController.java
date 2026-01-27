package com.example.base_blog.controller;

import com.example.base_blog.common.Result;
import com.example.base_blog.dto.UserRegisterRequest;
import com.example.base_blog.dto.UserResponse;
import com.example.base_blog.entity.Users;
import com.example.base_blog.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    private Result<UserResponse> register(@Valid @RequestBody UserRegisterRequest request){
        Users user = authService.register(request);
        UserResponse userResponse = new UserResponse();
        userResponse.setUsername(user.getUsername());
        return Result.success(userResponse);
    }

}
