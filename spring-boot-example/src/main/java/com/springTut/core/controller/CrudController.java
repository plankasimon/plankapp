package com.springTut.core.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.springTut.core.request.CrudRequest;
import com.springTut.core.response.CrudResponse;
import com.springTut.core.service.CrudService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/crud")
@RequiredArgsConstructor
public class CrudController {

    private final CrudService service;

    @GetMapping("/delete/{id}")
    public ResponseEntity<CrudResponse> deleteUser(@PathVariable Integer id) {
        try {
            CrudResponse crudResponse = service.delete(id);
            return new ResponseEntity<>(crudResponse, crudResponse.getStatus());
        } catch (Exception e) {
            CrudResponse crudResponse = CrudResponse.builder()
                    .succes(false)
                    .body(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
            return new ResponseEntity<>(crudResponse, crudResponse.getStatus());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CrudResponse> updateUser(@PathVariable("id") Integer id, @RequestBody CrudRequest request) {
        try {
            CrudResponse response = service.updateUser(id, request);
            return new ResponseEntity<>(response, response.getStatus());
        } catch (Exception e) {
            CrudResponse response = CrudResponse.builder()
                    .body(e.getMessage())
                    .succes(false)
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
            return new ResponseEntity<>(response, response.getStatus());
        }
    }

    @GetMapping("/list")
    public ResponseEntity<CrudResponse> listUser() {
        try {
            CrudResponse response = service.listUsers();
            return new ResponseEntity<>(response, response.getStatus());
        } catch (Exception e) {
            CrudResponse response = CrudResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .succes(false)
                    .body(e.getMessage())
                    .build();
            return new ResponseEntity<>(response, response.getStatus());
        }
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<CrudResponse> readUser(@PathVariable Integer id) {
        try {
            CrudResponse response = service.readUser(id);
            return new ResponseEntity<>(response, response.getStatus());
        } catch (Exception e) {
            CrudResponse response = CrudResponse.builder()
                    .body(e.getMessage())
                    .succes(false)
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
            return new ResponseEntity<>(response, response.getStatus());
        }
    }
}
