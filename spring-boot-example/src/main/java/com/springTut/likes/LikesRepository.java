package com.springTut.likes;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LikesRepository extends JpaRepository<Likes, Integer> {

    @Query("SELECT l.liked FROM Like l WHERE l.postId = :postId AND l.userId = :userId")
    Optional<Boolean> isPostLikedByUser(Integer postId, Integer userId);

    @Query("SELECT l FROM Like l WHERE l.postId = :postId AND l.userId = :userId")
    Optional<Likes> findByPostAndUser(Integer postId, Integer userId);

}
