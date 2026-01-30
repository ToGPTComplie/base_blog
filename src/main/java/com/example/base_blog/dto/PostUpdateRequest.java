package com.example.base_blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostUpdateRequest {
    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    private String content;

    @Size(max = 512)
    private String summary;
}
