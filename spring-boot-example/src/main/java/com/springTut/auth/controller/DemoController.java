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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class DemoController {

    private final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    private final AuthenticationService authService;

    @GetMapping("/user")
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
}
