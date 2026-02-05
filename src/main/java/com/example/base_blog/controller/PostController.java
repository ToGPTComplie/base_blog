package com.example.base_blog.controller;

import com.example.base_blog.common.Result;
import com.example.base_blog.dto.PostCreateRequest;
import com.example.base_blog.dto.PostResponse;
import com.example.base_blog.entity.Posts;
import com.example.base_blog.service.post.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    @PostMapping("/create")
    public Result createPosts(@Valid @RequestBody PostCreateRequest request){
        Posts post = postService.createPosts(request);
        return Result.success(post.getId());
    }

    @PostMapping("/update/{id}")
    public Result updatePosts(@PathVariable Long id, @Valid @RequestBody PostCreateRequest request){
        Posts post = postService.updatePosts(id, request);
        return Result.success(post.getId());
    }

    @PostMapping("/publish/{id}")
    public Result publishPosts(@PathVariable Long id){
        postService.publishPost(id);
        return Result.success("文章上传成功");
    }

    @GetMapping("/get/list")
    public Result<Page<PostResponse>> getList(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        return Result.success(postService.getPostsList(pageable));
    }

    @GetMapping("/get/{id}")
    public Result<PostResponse> getPost(@PathVariable Long id){
        return  Result.success(postService.getPostDetails(id));
    }

    @PostMapping("/delete/{id}")
    public Result deletePost(@PathVariable Long id){
        postService.deletePost(id);
        return Result.success("文章删除成功");
    }
}
