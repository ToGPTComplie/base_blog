package com.example.base_blog.service.post.impl;

import com.example.base_blog.dto.PostCreateRequest;
import com.example.base_blog.dto.PostResponse;
import com.example.base_blog.entity.PostStatus;
import com.example.base_blog.entity.Posts;
import com.example.base_blog.entity.Tags;
import com.example.base_blog.entity.Users;
import com.example.base_blog.exception.BusinessException;
import com.example.base_blog.repository.PostsRepository;
import com.example.base_blog.repository.TagsRepository;
import com.example.base_blog.repository.UsersRepository;
import com.example.base_blog.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.base_blog.common.ResultCode.*;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostsRepository postsRepository;
    private final UsersRepository usersRepository;
    private final TagsRepository tagsRepository;

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

        handleTags(post, request.getTags());

        return  postsRepository.save(post);
    }


    private void handleTags(Posts post, List<String> tagNames) {
        if (tagNames == null || tagNames.isEmpty()) {
            return;
        }
        for (String name : tagNames) {
            Tags tag = tagsRepository.findByName(name).orElseGet(() -> tagsRepository.save(new Tags(name)));
            post.addTag(tag);
        }
    }

    @Override
    @Transactional
    public Posts updatePosts(Long id, PostCreateRequest request) {
        Posts post = postsRepository.findById(id).orElseThrow(() -> new BusinessException(ARTICLE_NOT_EXIT));

        String currentUsername = getCurrentUsername();

        if(!post.getAuthor().getUsername().equals(currentUsername)){
            throw new BusinessException(NOT_AUTHOR);
        }

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setSummary(request.getSummary());

        updateTags(post, request.getTags());

        return  postsRepository.save(post);

    }

    private void updateTags(Posts post, List<String> newTagsName) {
        if (newTagsName == null || newTagsName.isEmpty()) {
            return;
        }

        List<Tags> tagsToRemove = post.getTags().stream().filter(tag -> !newTagsName.contains(tag.getName())).toList();

        List<String> currentTagName = post.getTags().stream().map(Tags::getName).toList();

        List<String> tagsToAdd = newTagsName.stream().filter(name -> !currentTagName.contains(name)).toList();

        tagsToRemove.forEach(post::removeTag);

        handleTags(post, tagsToAdd);

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

    @Override
    public Page<PostResponse> getPostsList(Pageable pageable) {
        Page<Posts> posts = postsRepository.findAll(pageable);
        return posts.map( post -> convertToResponse(post,false));
    }

    @Override
    @Transactional
    public PostResponse getPostDetails(Long id) {
        Posts post = postsRepository.findById(id).orElseThrow(() -> new BusinessException(ARTICLE_NOT_EXIT));

        postsRepository.incrementPv(id);

        PostResponse postResponse = convertToResponse(post,true);
        postResponse.setPv(post.getPv()+1);

        return postResponse;
    }

    @Override
    @Transactional
    public void deletePost(Long id) {
        Posts post = postsRepository.findById(id).orElseThrow(() -> new BusinessException(ARTICLE_NOT_EXIT));
        String currentUsername = getCurrentUsername();
        if (!post.getAuthor().getUsername().equals(currentUsername)){
            throw new BusinessException(NOT_AUTHOR);
        }
        postsRepository.delete(post);
    }

    private PostResponse convertToResponse(Posts posts, Boolean withContent) {
        PostResponse postResponse = new PostResponse();
        postResponse.setId(posts.getId());
        postResponse.setTitle(posts.getTitle());
        postResponse.setSummary(posts.getSummary());
        if (withContent){
            postResponse.setContent(posts.getContent());
        }
        postResponse.setStatus(posts.getStatus());
        postResponse.setPv(posts.getPv());
        postResponse.setCreateTime(posts.getCreatedAt());
        postResponse.setUpdateTime(posts.getUpdatedAt());

        List<String> tagNames = posts.getTags().stream().map(Tags::getName).toList();
        postResponse.setTags(tagNames);

        if (posts.getAuthor() != null){
            PostResponse.UserInfo userInfo = new PostResponse.UserInfo();
            userInfo.setId(posts.getAuthor().getId());
            userInfo.setUsername(posts.getAuthor().getUsername());
            userInfo.setNickname(posts.getAuthor().getNickname());
            postResponse.setAuthor(userInfo);

        }

        return postResponse;
    }

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return principal.toString();
    }
}
