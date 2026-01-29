package com.example.base_blog.controller;

import com.example.base_blog.common.Result;
import com.example.base_blog.dto.PostCreateRequest;
import com.example.base_blog.entity.Posts;
import com.example.base_blog.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    @PostMapping("/create")
    public Result createPosts(@RequestBody PostCreateRequest request){
        Posts post = postService.createPosts(request);
        return Result.success(post.getId());
    }

    @PostMapping("/{id}/publish")
    public Result publishPosts(@PathVariable Long id){
        postService.publishPost(id);
        return Result.success("文章上传成功");
    }


}
