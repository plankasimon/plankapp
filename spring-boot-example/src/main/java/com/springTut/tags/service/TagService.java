package com.springTut.tags.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.springTut.post.PostRepository;
import com.springTut.tags.PostTagsRepository;
import com.springTut.tags.Tags;
import com.springTut.tags.TagsRepository;
import com.springTut.tags.request.TagRequest;
import com.springTut.tags.response.TagResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagsRepository tagsRepository;

    public TagResponse createTag(TagRequest request) {
        var tag = Tags.builder()
                .tagName(request.getTagName())
                .build();
        tagsRepository.save(tag);
        return TagResponse.builder()
                .status(HttpStatus.OK)
                .success(true)
                .body("Tag created successfully: " + request.getTagName())
                .build();
    }


    public TagResponse readTag(Integer id) {
        Tags tags = tagsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tag id"));
        return TagResponse.builder()
                .status(HttpStatus.OK)
                .success(true)
                .body(tags.getTagName())
                .build();
    }

    public TagResponse updateTag(Integer id, TagRequest request) {
        var tagName = request.getTagName();
        Tags tags = tagsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tag id"));
        tags.setTagName(tagName);
        tagsRepository.save(tags);
        return TagResponse.builder()
                .status(HttpStatus.OK)
                .success(true)
                .body("Post: " + tagName + ", has been updated")
                .build();
    }

    public TagResponse deleteTag(Integer id){
        Tags tags = tagsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tag id"));
        tagsRepository.delete(tags);
        return TagResponse.builder()
                .status(HttpStatus.OK)
                .success(true)
                .body("Tag with id: " + id + " deleted")
                .build();
    }

    public TagResponse listTags(){
        List<Tags> tagsList = tagsRepository.findAll();
        return TagResponse.builder()
                .status(HttpStatus.OK)
                .success(true)
                .tagsList(tagsList)
                .build();
    }

}
