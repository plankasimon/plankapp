package com.springTut.core.response;

import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.springTut.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CrudResponse {

    private String body;
    private boolean succes;
    private HttpStatus status;
    private List<User> userList;
    private User userRead;
}
