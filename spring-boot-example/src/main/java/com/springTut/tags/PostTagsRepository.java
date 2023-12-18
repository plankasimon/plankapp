package com.springTut.tags;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostTagsRepository extends JpaRepository<PostTags, Integer> {

    @Query("select t from post_tags t where t.postId = :postId AND t.tagId = :tagId")
    Optional<PostTags> findByTagIdAndPostId(Integer tagId, Integer postId);
}
