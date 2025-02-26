package com.temm.skillify.controller;

import com.temm.skillify.service.SimulatedResultServiceMentor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/simulatedResultMentor")
public class SimulatedResultControllerMentor {

    @Autowired
    private SimulatedResultServiceMentor simulatedResultServiceMentor;

    @PostMapping
    public ResponseEntity create() {
        simulatedResultServiceMentor.create();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity edit() {
        simulatedResultServiceMentor.edit();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity get() {
        simulatedResultServiceMentor.get();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity getById() {
        simulatedResultServiceMentor.getById();
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete() {
        simulatedResultServiceMentor.delete();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
