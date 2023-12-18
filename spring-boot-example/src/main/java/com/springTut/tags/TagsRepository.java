package com.springTut.tags;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TagsRepository extends JpaRepository<Tags, Integer> {
    @Query("select t from Tags t where t.tagName = :tagName")
    Optional<Tags> findTagsByTagName(String tagName);
}
