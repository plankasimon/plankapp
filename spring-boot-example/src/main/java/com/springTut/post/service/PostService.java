package com.springTut.post.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.springTut.likes.Likes;
import com.springTut.likes.LikesRepository;
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
    private final LikesRepository likesRepository;

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
            for (String tagName : request.getTags()) {
                var newTagSingle = tagsRepository.findTagByTagName(tagName);
                if (newTagSingle.isEmpty()) {
                    var tag = Tags.builder()
                            .tagName(tagName)
                            .build();
                    tagsRepository.save(tag);
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
            for (String tagName : request.getTags()) {
                var newTagSingle = tagsRepository.findTagByTagName(tagName);
                if (newTagSingle.isEmpty()) {
                    var tag = Tags.builder()
                            .tagName(tagName)
                            .build();
                    tagsRepository.save(tag);
                }
                var newTagId = tagsRepository.findTagByTagName(tagName)
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

        for (String tagName : request.getTags()) {
            var tag = tagsRepository.findTagByTagName(tagName)
                    .orElseThrow(() -> new IllegalArgumentException("No tag found"));
            PostTags postTags = postTagsRepository.findByTagIdAndPostId(tag.getId(), post.getId())
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

    public PostResponse likePost(Integer postId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = ((UserDetails) auth.getPrincipal()).getUsername();

        Integer currentUserId = userRepository
                .findByEmail(currentUser)
                .orElseThrow(() -> new IllegalArgumentException("User error"))
                .getId();

        var likeStatus = likesRepository.isPostLikedByUser(postId, currentUserId);
        var message = "";

        if (likeStatus.isPresent()) {
            var isLiked = likeStatus.get();
            if (isLiked) {
                deleteLikeOrDislike(postId, currentUserId);
                message = "Post like successfully";
                Likes.builder()
                        .liked(true)
                        .userId(currentUserId)
                        .postId(postId)
                        .build();
            } else {
                message = "Like removed";
                deleteLikeOrDislike(postId,currentUserId);
            }
        }else {
            message = "Post liked successfully";
            Likes.builder()
                    .liked(true)
                    .userId(currentUserId)
                    .postId(postId)
                    .build();
        }

        return PostResponse.builder()
                .status(HttpStatus.OK)
                .success(true)
                .body(message)
                .build();
    }

    public PostResponse dislikePost(Integer postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = ((UserDetails) authentication.getPrincipal()).getUsername();

        Integer currentUserId = userRepository
                .findByEmail(currentUser)
                .orElseThrow(() -> new IllegalArgumentException("User error"))
                .getId();

        var likeStatus = likesRepository.isPostLikedByUser(postId, currentUserId);
        var message = "";

        if (likeStatus.isPresent()) {
           var isLiked = likeStatus.get();
           if (isLiked) {
               deleteLikeOrDislike(postId, currentUserId);
               message = "Post disliked successfully";
               Likes.builder()
                       .liked(false)
                       .userId(currentUserId)
                       .postId(postId)
                       .build();
           } else {
               message = "Dislike removed";
               deleteLikeOrDislike(postId,currentUserId);
           }
        }else {
            message = "Post disliked successfully";
            Likes.builder()
                    .liked(false)
                    .userId(currentUserId)
                    .postId(postId)
                    .build();
        }

        return PostResponse.builder()
                .status(HttpStatus.OK)
                .success(true)
                .body(message)
                .build();
    }

    private void deleteLikeOrDislike(Integer postId, Integer userId) {
        var LikeRecord = likesRepository.findByPostAndUser(postId, userId)
                        .orElseThrow(() -> new IllegalArgumentException("No like record found"));
        likesRepository.delete(LikeRecord);
    }
}
