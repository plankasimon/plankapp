package com.springTut.friendSystem.response;

import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.springTut.friendSystem.FriendSystem;
import com.springTut.user.UserProjection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FriendResponse {

    private String body;

    private boolean success;

    private HttpStatus status;

    private List<FriendSystem> friendRequestList;

    private List<UserProjection> friends;
}
