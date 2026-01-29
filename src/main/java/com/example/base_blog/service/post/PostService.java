package com.example.base_blog.service.post;

import com.example.base_blog.dto.PostCreateRequest;
import com.example.base_blog.entity.Posts;

public interface PostService {

    Posts createPosts(PostCreateRequest request);

    void publishPost(Long id);
}
