package com.springTut.friendSystem.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.springTut.friendSystem.FriendSystem;
import com.springTut.friendSystem.FriendSystemRepository;
import com.springTut.friendSystem.request.FriendRequest;
import com.springTut.friendSystem.response.FriendResponse;
import com.springTut.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendSystemRepository friendSystemRepository;

    private final UserRepository userRepository;

    public FriendResponse createFriendRequest(FriendRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof UserDetails)) {
            throw new IllegalArgumentException("No user");
        }

        var user = ((UserDetails) auth.getPrincipal()).getUsername();

        var user1 = userRepository.findByEmail(user)
                .orElseThrow(() -> new IllegalArgumentException("No user logged in"));

        var user2 = userRepository.findById(request.getUser2())
                .orElseThrow(() -> new IllegalArgumentException("User id not provided"));

        if (user1 == user2) {
            throw new IllegalArgumentException("Cannot send yourself a friend request");
        }

        var friendRequestA = friendSystemRepository.findFriendRequests(user1.getId());

        if (!friendRequestA.isEmpty()) {
            throw new IllegalArgumentException("Request already sent");
        }

        var friendExists = friendSystemRepository.existsFriend(user1.getId(), user2.getId());

        if (friendExists) {
            throw new IllegalArgumentException("You are already friends");
        }

        var friendRequest = FriendSystem.builder()
                .userId1(user1.getId())
                .userId2(user2.getId())
                .status("unresolved")
                .build();
        friendSystemRepository.save(friendRequest);

        return FriendResponse.builder()
                .status(HttpStatus.OK)
                .success(true)
                .body("Friend request sent")
                .build();
    }

    public FriendResponse acceptFriendRequest(FriendRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof UserDetails)) {
            throw new IllegalArgumentException("No user");
        }

        var activeUser = ((UserDetails) auth.getPrincipal()).getUsername();

        var user1 = userRepository.findByEmail(activeUser)
                .orElseThrow(() -> new IllegalArgumentException("No user logged in"));

        var user2 = userRepository.findById(request.getUser2())
                .orElseThrow(() -> new IllegalArgumentException("No user found with this id: " + request.getUser2()));

        var friendRequest = friendSystemRepository.findFriendRequestByUserAndId(user1.getId(), user2.getId());

        if (friendRequest == null) {
            throw new IllegalArgumentException("No friend requests");
        }

        friendRequest.setStatus("resolved");
        friendSystemRepository.save(friendRequest);

        return FriendResponse.builder()
                .status(HttpStatus.OK)
                .success(true)
                .body("Friend request accepted")
                .build();
    }

    public FriendResponse listFriendRequests() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof UserDetails)) {
            throw new IllegalArgumentException("No user");
        }

        var activeUser = ((UserDetails) auth.getPrincipal()).getUsername();

        var user = userRepository.findByEmail(activeUser)
                .orElseThrow(() -> new IllegalArgumentException("No active user"));

        var friendRequests = friendSystemRepository.findFriendRequests(user.getId());

        return FriendResponse.builder()
                .status(HttpStatus.OK)
                .success(true)
                .friendRequestList(friendRequests)
                .build();
    }

    public FriendResponse listFriends() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof UserDetails)) {
            throw new IllegalArgumentException("No user");
        }

        var activeUser = ((UserDetails) auth.getPrincipal()).getUsername();

        var user = userRepository.findByEmail(activeUser)
                .orElseThrow(() -> new IllegalArgumentException("No active user"));

        var friends = friendSystemRepository.findAllFriends(user.getId());

        return FriendResponse.builder()
                .status(HttpStatus.OK)
                .success(true)
                .friends(friends)
                .build();
    }

}
