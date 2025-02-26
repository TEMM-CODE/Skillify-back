package com.temm.skillify.controller;

import com.temm.skillify.service.QuestionServiceMentor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/questionMentor")
public class QuestionControllerMentor {

    @Autowired
    private QuestionServiceMentor questionServiceMentor;

    @PostMapping
    public ResponseEntity create() {
        questionServiceMentor.create();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity edit() {
        questionServiceMentor.edit();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity get() {
        questionServiceMentor.get();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity getById() {
        questionServiceMentor.getById();
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete() {
        questionServiceMentor.delete();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
