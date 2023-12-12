package com.springTut.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springTut.auth.request.AuthenticationRequest;
import com.springTut.auth.request.RegisterRequest;
import com.springTut.auth.response.AuthenticationResponse;
import com.springTut.auth.response.RegisterResponse;
import com.springTut.auth.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        try {
            RegisterResponse response = service.register(request);
            return new ResponseEntity<>(response, response.getStatus());
        } catch (Exception e) {
            RegisterResponse response = RegisterResponse.builder()
                    .succes(false)
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage())
                    .build();
            return new ResponseEntity<>(response, response.getStatus());
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        try {
            AuthenticationResponse response = service.authenticate(request);
            return new ResponseEntity<>(response, response.getStatus());
        } catch (Exception e) {
            AuthenticationResponse response = AuthenticationResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage())
                    .success(false)
                    .build();
            return new ResponseEntity<>(response, response.getStatus());
        }
    }
}
