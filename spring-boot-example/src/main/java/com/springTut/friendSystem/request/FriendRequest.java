package com.springTut.friendSystem.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.springTut.friendSystem.FriendSystem;
import com.springTut.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FriendRequest {

    private Integer user1; //User sending request

    private Integer user2;

    private List<User> friendSystemList;

}
