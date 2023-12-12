package com.springTut.core.service;

import java.util.List;

import org.springframework.http.HttpStatus;
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
    public CrudResponse readUser(Integer id){
        var userRead = repository.findById(id);
        return CrudResponse.builder()
                .userRead(userRead)
                .status(HttpStatus.OK)
                .succes(true)
                .build();
    }

    public CrudResponse updateUser(Integer id, CrudRequest request) {
        var email = request.getEmail();
        var firstname = request.getFirstname();
        var lastname = request.getLastname();
        User user = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));

        if (email.isEmpty()){
            throw new IllegalArgumentException("No email in request");
        }
        if (firstname.isEmpty()){
            throw new IllegalArgumentException("No firstname in request");
        }
        if(lastname.isEmpty()){
            throw new IllegalArgumentException("No lastname in request");
        }
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
