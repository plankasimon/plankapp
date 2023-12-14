package com.springTut.post;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query("select p from Post p where p.userId = :id")
    List<Post> getAllPostsById(Integer id);
}
