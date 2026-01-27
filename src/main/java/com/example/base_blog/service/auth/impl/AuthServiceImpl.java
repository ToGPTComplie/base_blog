package com.example.base_blog.service.auth.impl;

import com.example.base_blog.dto.UserRegisterRequest;
import com.example.base_blog.entity.Users;
import com.example.base_blog.exception.BusinessException;
import com.example.base_blog.mapper.UsersMapper;
import com.example.base_blog.repository.UsersRepository;
import com.example.base_blog.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.base_blog.common.ResultCode.USER_ALREADY_EXIST;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UsersMapper usersMapper;

    @Transactional
    @Override
    public Users register(@Valid UserRegisterRequest request){

        List<Users> existingUsers = usersMapper.selectByRegisterInfo(request);

        if (!existingUsers.isEmpty()) {
            List<String> errors = new ArrayList<>();

            for(Users user : existingUsers){
                if (request.getUsername().equals(user.getUsername())) {
                    errors.add("用户名已存在");
                }
                if (request.getMobile() != null && request.getMobile().equals(user.getMobile())) {
                    errors.add("手机号已注册");
                }
                if (request.getEmail() != null && request.getEmail().equals(user.getEmail())) {
                    errors.add("邮箱已注册");
                }
            }

            String uniqueErrors = errors.stream().distinct().collect(Collectors.joining("\n"));

            if (!uniqueErrors.isEmpty()) {
                throw new BusinessException(USER_ALREADY_EXIST.getCode(), uniqueErrors);
            }
        }

        Users user = new Users();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setNickname(request.getNickname());
        user.setMobile(request.getMobile());

        return usersRepository.save(user);
    }
}
