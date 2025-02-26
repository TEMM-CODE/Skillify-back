package com.temm.skillify.controller;

import com.temm.skillify.service.UserServiceMentor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userMentor")
public class UserControllerMentor {

    @Autowired
    private UserServiceMentor userServiceMentor;

    @PostMapping
    public ResponseEntity create() {
        userServiceMentor.create();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity edit() {
        userServiceMentor.edit();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity get() {
        userServiceMentor.get();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity getById() {
        userServiceMentor.getById();
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete() {
        userServiceMentor.delete();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
