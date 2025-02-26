package com.temm.skillify.controller;

import com.temm.skillify.service.ClassServiceAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/classAdmin")
public class ClassControllerAdmin {

    @Autowired
    private ClassServiceAdmin classServiceAdmin;

    @PostMapping
    public ResponseEntity create() {
        classServiceAdmin.create();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity edit() {
        classServiceAdmin.edit();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity get() {
        classServiceAdmin.get();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity getById() {
        classServiceAdmin.getById();
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete() {
        classServiceAdmin.delete();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
