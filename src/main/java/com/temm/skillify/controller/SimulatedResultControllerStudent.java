package com.temm.skillify.controller;

import com.temm.skillify.service.SimulatedResultServiceStudent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/simulatedResultStudent")
public class SimulatedResultControllerStudent {

    @Autowired
    private SimulatedResultServiceStudent simulatedResultServiceStudent;

    @PostMapping
    public ResponseEntity create() {
        simulatedResultServiceStudent.create();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity edit() {
        simulatedResultServiceStudent.edit();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity get() {
        simulatedResultServiceStudent.get();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity getById() {
        simulatedResultServiceStudent.getById();
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete() {
        simulatedResultServiceStudent.delete();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
