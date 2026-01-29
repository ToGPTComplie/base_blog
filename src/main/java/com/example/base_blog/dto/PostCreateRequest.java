package com.example.base_blog.dto;

import com.example.base_blog.entity.PostStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostCreateRequest {
    @NotBlank(message = "标题不为空")
    @Size(max = 255, message = "标题长度不能超过255")
    private String title;

    @Size(max = 512, message = "摘要长度不能超过512")
    private String summary;

    @NotBlank(message = "内容不为空")
    private String content;

    private PostStatus status;
}
