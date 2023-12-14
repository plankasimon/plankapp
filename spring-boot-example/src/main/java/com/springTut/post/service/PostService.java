package com.springTut.post.service;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.springTut.post.Post;
import com.springTut.post.PostRepository;
import com.springTut.post.request.PostRequest;
import com.springTut.post.response.PostResponse;
import com.springTut.user.User;
import com.springTut.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostResponse createPost(PostRequest request) {
        var user = userRepository.findById(request.getUserId()).orElseThrow(() -> new IllegalArgumentException("Invalid user id"));
        var post = Post.builder()
                .title(request.getTitle())
                .body(request.getBody())
                .userId(user.getId())
                .build();
        postRepository.save(post);
        return PostResponse.builder()
                .status(HttpStatus.OK)
                .success(true)
                .body("Post created successfully " + request.getTitle())
                .build();
    }

    public PostResponse readPost(Integer id) {
        Post postRead = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id: " + id));
        return PostResponse.builder()
                .status(HttpStatus.OK)
                .success(true)
                .body(postRead.getTitle() + " " + postRead.getBody())
                .build();
    }

    public PostResponse updatePost(Integer id, PostRequest request) {
        var user = userRepository.findById(request.getUserId()).orElseThrow(() -> new IllegalArgumentException("Invalid user id"));
        request.checkIsEmpty();
        var title = request.getTitle();
        var body = request.getBody();
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
        post.setTitle(title);
        post.setBody(body);
        post.setUserId(user.getId());
        postRepository.save(post);
        return PostResponse.builder()
                .status(HttpStatus.OK)
                .success(true)
                .body("Post: " + title + ", has been updated")
                .build();
    }

    public PostResponse deletePost(Integer id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id: " + id));
        postRepository.delete(post);
        return PostResponse.builder()
                .status(HttpStatus.OK)
                .success(true)
                .body("Post with id: " + id + " deleted")
                .build();
    }

    public PostResponse listPosts() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = ((UserDetails) auth.getPrincipal()).getUsername();
        Integer currentUserId = userRepository
                .findByEmail(currentUser)
                .orElseThrow(() -> new IllegalArgumentException("User error"))
                .getId();
        List<Post> postList = postRepository.getAllPostsById(currentUserId);
        return PostResponse.builder()
                .status(HttpStatus.OK)
                .success(true)
                .postList(postList)
                .build();
    }
}
