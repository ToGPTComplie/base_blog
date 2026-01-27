package com.example.base_blog.mapper;

import com.example.base_blog.dto.UserRegisterRequest;
import com.example.base_blog.entity.Users;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UsersMapper {

    List<Users> selectByRegisterInfo(UserRegisterRequest request);

}
