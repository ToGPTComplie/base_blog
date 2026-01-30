package com.example.base_blog.controller;

import com.example.base_blog.common.Result;
import com.example.base_blog.dto.PostCreateRequest;
import com.example.base_blog.dto.PostResponse;
import com.example.base_blog.entity.Posts;
import com.example.base_blog.service.post.PostService;
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
    public Result createPosts(@RequestBody PostCreateRequest request){
        Posts post = postService.createPosts(request);
        return Result.success(post.getId());
    }

    @PostMapping("/{id}/publish")
    public Result publishPosts(@PathVariable Long id){
        postService.publishPost(id);
        return Result.success("文章上传成功");
    }

    @GetMapping("/get/list")
    public Result<Page<PostResponse>> getList(@PageableDefault(sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable){
        return Result.success(postService.getPostsList(pageable));
    }


}
