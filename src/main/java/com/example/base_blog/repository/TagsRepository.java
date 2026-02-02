package com.example.base_blog.repository;

import com.example.base_blog.entity.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagsRepository extends JpaRepository<Tags,Long> {
    Optional<Tags> findByName(String name);
}
