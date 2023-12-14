package com.springTut.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springTut.auth.response.UserResponse;
import com.springTut.auth.service.AuthenticationService;
import com.springTut.post.response.PostResponse;
import com.springTut.post.service.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationService authService;
    private final PostService service;

    @GetMapping("/get-user")
    public ResponseEntity<UserResponse> user() {
        try {
            UserResponse response = authService.user();
            return new ResponseEntity<>(response, response.getStatus());
        } catch (Exception e) {
            UserResponse response = UserResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .success(false)
                    .body(e.getMessage())
                    .build();
            return new ResponseEntity<>(response, response.getStatus());
        }
    }
    @GetMapping("/list-posts")
    public ResponseEntity<PostResponse> listPosts() {
        try {
            PostResponse response = service.listPosts();
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
