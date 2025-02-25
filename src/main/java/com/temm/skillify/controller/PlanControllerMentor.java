package com.temm.skillify.controller;

import com.temm.skillify.service.PlanServiceMentor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/planMentor")
public class PlanControllerMentor {

    @Autowired
    private PlanServiceMentor planServiceMentor;

    @PostMapping
    public ResponseEntity create() {
        planServiceMentor.create();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity edit() {
        planServiceMentor.edit();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity get() {
        planServiceMentor.get();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity getById() {
        planServiceMentor.getById();
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete() {
        planServiceMentor.delete();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
