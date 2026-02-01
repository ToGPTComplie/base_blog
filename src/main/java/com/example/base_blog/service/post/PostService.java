package com.example.base_blog.service.post;

import com.example.base_blog.dto.PostCreateRequest;
import com.example.base_blog.dto.PostResponse;
import com.example.base_blog.entity.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {

    Posts createPosts(PostCreateRequest request);

    Posts updatePosts(Long id, PostCreateRequest request);

    void publishPost(Long id);

    Page<PostResponse> getPostsList(Pageable  pageable);

    PostResponse getPostDetails(Long id);

    void deletePost(Long id);
}
