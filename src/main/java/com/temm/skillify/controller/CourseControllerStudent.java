package com.temm.skillify.controller;

import com.temm.skillify.service.CourseServiceStudent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courseStudent")
public class CourseControllerStudent {

    @Autowired
    private CourseServiceStudent courseServiceStudent;

    @PostMapping
    public ResponseEntity create() {
        courseServiceStudent.create();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity edit() {
        courseServiceStudent.edit();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity get() {
        courseServiceStudent.get();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity getById() {
        courseServiceStudent.getById();
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete() {
        courseServiceStudent.delete();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
