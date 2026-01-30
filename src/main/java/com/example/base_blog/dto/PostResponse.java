package com.example.base_blog.dto;

import com.example.base_blog.entity.PostStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostResponse {
    private Long id;
    private String title;
    private String summary;
    private PostStatus status;
    private Long pv;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    private UserInfo author;

    @Data
    public static class UserInfo{
        private Long id;
        private String username;
        private String nickname;
    }

}
