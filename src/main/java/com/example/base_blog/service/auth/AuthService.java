package com.example.base_blog.service.auth;


import com.example.base_blog.dto.UserRegisterRequest;
import com.example.base_blog.entity.Users;
import jakarta.validation.Valid;

public interface AuthService {
    public Users register(@Valid UserRegisterRequest request);
}
