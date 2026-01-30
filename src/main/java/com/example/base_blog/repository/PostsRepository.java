package com.example.base_blog.repository;

import com.example.base_blog.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface PostsRepository extends JpaRepository<Posts, Long> {

    @Modifying
    @Transactional
    @Query("update Posts p set p.pv = p.pv + 1 where p.id = :id")
    void incrementPv( Long id);
}
