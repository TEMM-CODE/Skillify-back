package com.temm.skillify.controller;

import com.temm.skillify.service.UserServiceAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userAdmin")
public class UserControllerAdmin {

    @Autowired
    private UserServiceAdmin userServiceAdmin;

    @PostMapping
    public ResponseEntity create() {
        userServiceAdmin.create();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity edit() {
        userServiceAdmin.edit();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity get() {
        userServiceAdmin.get();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity getById() {
        userServiceAdmin.getById();
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete() {
        userServiceAdmin.delete();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
