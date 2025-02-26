package com.temm.skillify.controller;

import com.temm.skillify.service.ClassServiceStudent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/classStudent")
public class ClassControllerStudent {
    @Autowired
    private ClassServiceStudent classServiceStudent;

    @PostMapping
    public ResponseEntity create() {
        classServiceStudent.create();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity edit() {
        classServiceStudent.edit();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity get() {
        classServiceStudent.get();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity getById() {
        classServiceStudent.getById();
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete() {
        classServiceStudent.delete();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
