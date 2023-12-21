package com.springTut.post.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.springTut.post.Post;
import com.springTut.post.PostRepository;
import com.springTut.post.request.PostRequest;
import com.springTut.post.response.PostResponse;
import com.springTut.tags.PostTags;
import com.springTut.tags.PostTagsRepository;
import com.springTut.tags.Tags;
import com.springTut.tags.TagsRepository;
import com.springTut.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TagsRepository tagsRepository;
    private final PostTagsRepository postTagsRepository;

    public PostResponse createPost(PostRequest request) {
        var user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user id"));
        var post = Post.builder()
                .title(request.getTitle())
                .body(request.getBody())
                .userId(user.getId())
                .build();
        postRepository.save(post);

        var newTag = tagsRepository.findTagByTagName(request.getTag());
        if (newTag.isEmpty()) {
            var tag = Tags.builder()
                    .tagName(request.getTag())
                    .build();
            tagsRepository.save(tag);
        }

        var newTagId = tagsRepository.findTagByTagName(request.getTag())
                .orElseThrow(() -> new IllegalArgumentException("No tag"));
        var postTag = PostTags.builder()
                .postId(request.getUserId())
                .tagId(newTagId.getId())
                .build();
        postTagsRepository.save(postTag);

        return PostResponse.builder()
                .status(HttpStatus.OK)
                .success(true)
                .body("Post created successfully " + request.getTitle())
                .build();
    }

    public PostResponse createPostMultipleTags(PostRequest request) {
        var user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user id"));
        var post = Post.builder()
                .title(request.getTitle())
                .body(request.getBody())
                .userId(user.getId())
                .build();
        postRepository.save(post);

        var newTag = tagsRepository.findTagsByTagName(request.getTags());

        if (!newTag.containsAll(request.getTags())) {
            for (String tag : request.getTags()) {
                var newTagSingle = tagsRepository.findTagByTagName(tag);
                if (newTagSingle.isEmpty()) {
                    var tag_ = Tags.builder()
                            .tagName(tag)
                            .build();
                    tagsRepository.save(tag_);
                }
            }
        }

        var newTagId = tagsRepository.findTagsByTagName(request.getTags());
        for (Tags tag : newTagId) {
            var postTag = PostTags.builder()
                    .postId(request.getUserId())
                    .tagId(tag.getId())
                    .build();
            postTagsRepository.save(postTag);
        }

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
        var user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user id"));
        request.checkIsEmpty();
        var title = request.getTitle();
        var body = request.getBody();

        var newTags = tagsRepository.findTagsByTagName(request.getTags());
        if (!newTags.containsAll(request.getTags())) {
            for (String tag : request.getTags()) {
                var newTagSingle = tagsRepository.findTagByTagName(tag);
                if (newTagSingle.isEmpty()) {
                    var tag_ = Tags.builder()
                            .tagName(tag)
                            .build();
                    tagsRepository.save(tag_);
                }
                var newTagId = tagsRepository.findTagByTagName(tag)
                        .orElseThrow(() -> new IllegalArgumentException("No tag"));

                Post post = postRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid post Id: " + id));

                if (postTagsRepository.findByTagIdAndPostId(newTagId.getId(), post.getId()).isEmpty()) {
                    PostTags postTags = PostTags.builder()
                            .tagId(newTagId.getId())
                            .postId(post.getId())
                            .build();
                    postTagsRepository.save(postTags);
                }
            }
        } else {
            for (String tag : request.getTags()) {
                var tagId = tagsRepository.findTagByTagName(tag)
                        .orElseThrow(() -> new IllegalArgumentException("No tag"));

                Post post = postRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid post Id: " + id));
                if (postTagsRepository.findByTagIdAndPostId(tagId.getId(), post.getId()).isEmpty()) {
                    PostTags postTags = PostTags.builder()
                            .tagId(tagId.getId())
                            .postId(post.getId())
                            .build();
                    postTagsRepository.save(postTags);
                }
            }
        }

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

    public PostResponse deleteTagFromPost(Integer id, PostRequest request) {
        request.checkTagsEmpty();
        var tags = tagsRepository.findTagsByTagName(request.getTags());
        if (!tags.containsAll(request.getTags())) {
            throw new IllegalArgumentException("One of the tags does not exist");
        }

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id: " + id));

        for (String tag : request.getTags()) {
            var tag_ = tagsRepository.findTagByTagName(tag)
                    .orElseThrow(() -> new IllegalArgumentException("No tag found"));
            PostTags postTags = postTagsRepository.findByTagIdAndPostId(tag_.getId(), post.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Tag and Post not paired"));
            postTagsRepository.delete(postTags);
        }
        return PostResponse.builder()
                .status(HttpStatus.OK)
                .success(true)
                .body("Tags deleted successfully")
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
