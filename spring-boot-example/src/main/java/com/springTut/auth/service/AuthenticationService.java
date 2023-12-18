package com.springTut.auth.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springTut.auth.request.AuthenticationRequest;
import com.springTut.auth.request.RegisterRequest;
import com.springTut.auth.response.AuthenticationResponse;
import com.springTut.auth.response.RegisterResponse;
import com.springTut.auth.response.UserResponse;
import com.springTut.config.JwtService;
import com.springTut.user.Role;
import com.springTut.user.User;
import com.springTut.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public RegisterResponse register(RegisterRequest request) {
        if (repository.findByEmail(request.getEmail()).isPresent()){
            throw new IllegalArgumentException("User already exists");
        }

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return RegisterResponse.builder()
                .status(HttpStatus.OK)
                .succes(true)
                .body("User created successfully")
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("No user found"));
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .status(HttpStatus.OK)
                .body("Logged in successfully")
                .success(true)
                .token(jwtToken)
                .build();
    }

    public UserResponse user(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof UserDetails)){
            throw new IllegalArgumentException("No user");
        }

        var user = ((UserDetails) auth.getPrincipal()).getUsername();

        return UserResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .body("Currently active user:" + user)
                .build();
    }
}
