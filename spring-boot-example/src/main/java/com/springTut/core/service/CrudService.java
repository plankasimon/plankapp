package com.springTut.core.service;

import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.springTut.core.request.CrudRequest;
import com.springTut.core.response.CrudResponse;
import com.springTut.user.User;
import com.springTut.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CrudService {

    private final UserRepository repository;

    public CrudResponse delete(Integer id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = ((UserDetails) auth.getPrincipal()).getUsername();
        Integer currentUserId = repository
                .findByEmail(currentUser)
                .orElseThrow(() -> new IllegalArgumentException("User error"))
                .getId();
        if (!Objects.equals(currentUserId, id)) {
            return CrudResponse.builder()
                    .succes(false)
                    .body("Active user does not match provided ID")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        User user = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
        repository.delete(user);
        return CrudResponse.builder()
                .body("User with id " + id + " deleted")
                .succes(true)
                .status(HttpStatus.OK)
                .build();
    }

    public CrudResponse listUsers() {
        List<User> userList = repository.findAll();
        return CrudResponse.builder()
                .userList(userList)
                .status(HttpStatus.OK)
                .succes(true)
                .build();
    }

    public CrudResponse readUser(Integer id) {
        User userRead = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
        return CrudResponse.builder()
                .userRead(userRead)
                .status(HttpStatus.OK)
                .succes(true)
                .build();
    }

    public CrudResponse updateUser(Integer id, CrudRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = ((UserDetails) auth.getPrincipal()).getUsername();
        Integer currentUserId = repository
                .findByEmail(currentUser)
                .orElseThrow(() -> new IllegalArgumentException("User error"))
                .getId();
        if (!Objects.equals(currentUserId, id)) {
            return CrudResponse.builder()
                    .succes(false)
                    .body("Active user does not match provided ID")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        request.checkIsEmpty();
        var email = request.getEmail();
        var firstname = request.getFirstname();
        var lastname = request.getLastname();
        User user = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
        user.setEmail(email);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        repository.save(user);

        return CrudResponse.builder()
                .status(HttpStatus.OK)
                .succes(true)
                .body("User: " + email + " has been updated")
                .build();
    }

}
