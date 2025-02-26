package com.temm.skillify.controller;

import com.temm.skillify.service.CourseServiceMentor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courseMentor")
public class CourseControllerMentor {

    @Autowired
    private CourseServiceMentor courseServiceMentor;

    @PostMapping
    public ResponseEntity create() {
        courseServiceMentor.create();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity edit() {
        courseServiceMentor.edit();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity get() {
        courseServiceMentor.get();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity getById() {
        courseServiceMentor.getById();
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete() {
        courseServiceMentor.delete();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
