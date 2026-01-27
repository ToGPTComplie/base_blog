package com.example.base_blog.repository;

import com.example.base_blog.entity.Users;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    Users findByUsername(@NotBlank(message = "用户名不能为空") @Size(min=1, max=64) String username);

    Users findByEmail(String email);

    Users findByMobile(String mobile);
}
