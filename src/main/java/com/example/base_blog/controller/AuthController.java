package com.example.base_blog.controller;

import com.example.base_blog.common.Result;
import com.example.base_blog.dto.UserLoginRequest;
import com.example.base_blog.dto.UserRegisterRequest;
import com.example.base_blog.dto.UserResponse;
import com.example.base_blog.entity.Users;
import com.example.base_blog.service.auth.AuthService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public Result<UserResponse> register(@Valid @RequestBody UserRegisterRequest request){
        Users user = authService.register(request);
        UserResponse userResponse = new UserResponse();
        userResponse.setUsername(user.getUsername());
        return Result.success(userResponse);
    }

    @PostMapping("/login")
    public Result<String> login(@Valid @RequestBody UserLoginRequest request, HttpServletRequest httpServletRequest){
        UsernamePasswordAuthenticationToken unauthenticationToken = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        Authentication authentication = authenticationManager.authenticate(unauthenticationToken);
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        HttpSession session = httpServletRequest.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);

        return Result.success(authentication.getName()+"登录成功");
    }

    @PostMapping("/testLogin")
    public Result<UserResponse> testLogin(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();
        UserResponse userResponse = new UserResponse();
        userResponse.setUsername(username);

        return Result.success(userResponse);
    }
}
