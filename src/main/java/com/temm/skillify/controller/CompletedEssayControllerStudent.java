package com.temm.skillify.controller;

import com.temm.skillify.service.CompletedEssayServiceStudent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/completedEssayStudent")
public class CompletedEssayControllerStudent {

    @Autowired
    private CompletedEssayServiceStudent completedEssayServiceStudent;

    @PostMapping
    public ResponseEntity create() {
        completedEssayServiceStudent.create();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity edit() {
        completedEssayServiceStudent.edit();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity get() {
        completedEssayServiceStudent.get();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity getById() {
        completedEssayServiceStudent.getById();
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete() {
        completedEssayServiceStudent.delete();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
