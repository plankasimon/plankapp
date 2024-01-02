package com.springTut.friendSystem.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.springTut.friendSystem.request.FriendRequest;
import com.springTut.friendSystem.response.FriendResponse;
import com.springTut.friendSystem.service.FriendService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/friend")
public class FriendController {

    private final FriendService friendService;

    @PostMapping("/create-friend-request")
    public ResponseEntity<FriendResponse> createFriendRequest(@RequestBody FriendRequest request){
        try{
            FriendResponse response = friendService.createFriendRequest(request);
            return new ResponseEntity<>(response, response.getStatus());
        }catch (Exception e){
            FriendResponse response = FriendResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .success(false)
                    .body(e.getMessage())
                    .build();
            return new ResponseEntity<>(response, response.getStatus());
        }
    }

    @PutMapping("/accept-friend-request")
    public ResponseEntity<FriendResponse> acceptFriendRequest(@RequestBody FriendRequest request){
        try {
            FriendResponse response = friendService.acceptFriendRequest(request);
            return new ResponseEntity<>(response, response.getStatus());
        }catch (Exception e){
            FriendResponse response = FriendResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .success(false)
                    .body(e.getMessage())
                    .build();
            return new ResponseEntity<>(response, response.getStatus());
        }
    }

    @GetMapping("/friend-requests")
    public ResponseEntity<FriendResponse> listFriendRequests(){
        try {
            FriendResponse response = friendService.listFriendRequests();
            return new ResponseEntity<>(response, response.getStatus());
        }catch (Exception e){
            FriendResponse response = FriendResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .success(false)
                    .body(e.getMessage())
                    .build();
            return new ResponseEntity<>(response, response.getStatus());
        }
    }

    @GetMapping("/friends")
    public ResponseEntity<FriendResponse> listFriends(){
        try{
            FriendResponse response = friendService.listFriends();
            return new ResponseEntity<>(response, response.getStatus());
        }catch (Exception e){
            FriendResponse response = FriendResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .success(false)
                    .body(e.getMessage())
                    .build();
            return new ResponseEntity<>(response, response.getStatus());
        }
    }
}
