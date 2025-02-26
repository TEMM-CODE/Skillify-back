package com.temm.skillify.controller;

import com.temm.skillify.service.UserServiceStudent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userStudent")
public class UserControllerStudent {

    @Autowired
    private UserServiceStudent userServiceStudent;

    @PostMapping
    public ResponseEntity create() {
        userServiceStudent.create();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity edit() {
        userServiceStudent.edit();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity get() {
        userServiceStudent.get();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity getById() {
        userServiceStudent.getById();
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete() {
        userServiceStudent.delete();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
