package com.example.base_blog.dto;

import lombok.Data;

@Data
public class CommentResponse {
    private Long id;

    private String content;

    private Long postId;

    private Long authorId;

    private String authorName;

    private String postTitle;

    private java.time.LocalDateTime createdAt;
}
