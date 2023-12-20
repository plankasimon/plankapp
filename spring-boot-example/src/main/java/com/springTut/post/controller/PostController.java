package com.springTut.post.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.springTut.post.request.PostRequest;
import com.springTut.post.response.PostResponse;
import com.springTut.post.service.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/post")
public class PostController {

    private final PostService service;

    @PostMapping("/create-post")
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest request) {
        try {
            PostResponse response = service.createPost(request);
            return new ResponseEntity<>(response, response.getStatus());
        } catch (Exception e) {
            PostResponse response = PostResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .success(false)
                    .body(e.getMessage())
                    .build();
            return new ResponseEntity<>(response, response.getStatus());
        }
    }

    @GetMapping("/read-post/{id}")
    public ResponseEntity<PostResponse> readPost(@PathVariable Integer id) {
        try {
            PostResponse response = service.readPost(id);
            return new ResponseEntity<>(response, response.getStatus());
        } catch (Exception e) {
            PostResponse response = PostResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .success(false)
                    .body(e.getMessage())
                    .build();
            return new ResponseEntity<>(response, response.getStatus());
        }
    }

    @PutMapping("/update-post/{id}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Integer id, @RequestBody PostRequest request) {
        try {
            PostResponse response = service.updatePost(id, request);
            return new ResponseEntity<>(response, response.getStatus());
        } catch (Exception e) {
            PostResponse response = PostResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .success(false)
                    .body(e.getMessage())
                    .build();
            return new ResponseEntity<>(response, response.getStatus());
        }
    }

    @DeleteMapping("/delete-tag-from-post/{id}")
    public ResponseEntity<PostResponse> deleteTagFromPost(@PathVariable Integer id, @RequestBody PostRequest request) {
        try {
            PostResponse response = service.deleteTagFromPost(id, request);
            return new ResponseEntity<>(response, response.getStatus());
        } catch (Exception e) {
            PostResponse response = PostResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .success(false)
                    .body(e.getMessage())
                    .build();
            return new ResponseEntity<>(response, response.getStatus());
        }
    }

    @DeleteMapping("/delete-post/{id}")

    public ResponseEntity<PostResponse> deletePost(@PathVariable Integer id) {
        try {
            PostResponse response = service.deletePost(id);
            return new ResponseEntity<>(response, response.getStatus());
        } catch (Exception e) {
            PostResponse response = PostResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .success(false)
                    .body(e.getMessage())
                    .build();
            return new ResponseEntity<>(response, response.getStatus());
        }
    }
}
