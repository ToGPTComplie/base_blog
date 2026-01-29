package com.example.base_blog.service.post.impl;

import com.example.base_blog.dto.PostCreateRequest;
import com.example.base_blog.entity.PostStatus;
import com.example.base_blog.entity.Posts;
import com.example.base_blog.entity.Users;
import com.example.base_blog.exception.BusinessException;
import com.example.base_blog.repository.PostsRepository;
import com.example.base_blog.repository.UsersRepository;
import com.example.base_blog.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.base_blog.common.ResultCode.NOT_AUTHOR;
import static com.example.base_blog.common.ResultCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostsRepository postsRepository;
    private final UsersRepository usersRepository;

    @Override
    @Transactional
    public Posts createPosts(PostCreateRequest request) {

        String username = getCurrentUsername();
        Users currentUser = usersRepository.findByUsername(username).orElseThrow(() -> new BusinessException(USER_NOT_FOUND.getCode(), USER_NOT_FOUND.getMessage() + username));

        Posts post = new Posts();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setSummary(request.getSummary());
        post.setAuthor(currentUser);

        if (request.getStatus() != null){
            post.setStatus(request.getStatus());
        }
        else {
            post.setStatus(PostStatus.DRAFT);
        }

        post.setPv(0L);

        return  postsRepository.save(post);
    }

    @Override
    @Transactional
    public void publishPost(Long id) {
        Posts post = postsRepository.findById(id).orElseThrow(() -> new BusinessException(404, "文章不存在"));

        String currentUsername = getCurrentUsername();
        if (!post.getAuthor().getUsername().equals(currentUsername)){
            throw new BusinessException(NOT_AUTHOR);
        }

        if (post.getStatus().equals(PostStatus.DRAFT)){
            post.setStatus(PostStatus.PUBLISHED);
        }

        postsRepository.save(post);
    }

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return principal.toString();
    }
}
