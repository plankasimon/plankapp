package com.springTut.tags.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.springTut.tags.request.TagRequest;
import com.springTut.tags.response.TagResponse;
import com.springTut.tags.service.TagService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagService service;

    @PostMapping("/create-tag")
    public ResponseEntity<TagResponse> createTag(@RequestBody TagRequest request) {
        try {
            TagResponse tagResponse = service.createTag(request);
            return new ResponseEntity<>(tagResponse, tagResponse.getStatus());
        } catch (Exception e) {
            TagResponse tagResponse = TagResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .success(false)
                    .body(e.getMessage())
                    .build();
            return new ResponseEntity<>(tagResponse, tagResponse.getStatus());
        }
    }

    @GetMapping("/read-tag/{id}")
    public ResponseEntity<TagResponse> readTag(@PathVariable Integer id) {
        try {
            TagResponse tagResponse = service.readTag(id);
            return new ResponseEntity<>(tagResponse, tagResponse.getStatus());
        }catch (Exception e){
            TagResponse tagResponse = TagResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .success(false)
                    .body(e.getMessage())
                    .build();
            return new ResponseEntity<>(tagResponse, tagResponse.getStatus());
        }
    }
    @PutMapping("/update-tag/{id}")
    public ResponseEntity<TagResponse> updateTag(@PathVariable Integer id, @RequestBody TagRequest request){
        try {
            TagResponse response = service.updateTag(id, request);
            return new ResponseEntity<>(response, response.getStatus());
        }catch (Exception e){
            TagResponse response = TagResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .success(false)
                    .body(e.getMessage())
                    .build();
            return new ResponseEntity<>(response, response.getStatus());
        }
    }

    @DeleteMapping("/delete-tag/{id}")
    public ResponseEntity<TagResponse> deleteTag(@PathVariable Integer id){
        try {
            TagResponse response = service.deleteTag(id);
            return new ResponseEntity<>(response, response.getStatus());
        }catch (Exception e){
            TagResponse response = TagResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .success(false)
                    .body(e.getMessage())
                    .build();
            return new ResponseEntity<>(response, response.getStatus());
        }
    }

    @GetMapping("/list-tags")
    public ResponseEntity<TagResponse> listTags(){
        try {
            TagResponse response = service.listTags();
            return new ResponseEntity<>(response, response.getStatus());
        }catch (Exception e){
            TagResponse response = TagResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .success(false)
                    .body(e.getMessage())
                    .build();
            return new ResponseEntity<>(response, response.getStatus());
        }
    }
}
