package com.temm.skillify.controller;

import com.temm.skillify.service.PlanServiceAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/planAdmin")
public class PlanControllerAdmin {

    @Autowired
    private PlanServiceAdmin planServiceAdmin;

    @PostMapping
    public ResponseEntity create() {
        planServiceAdmin.create();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity edit() {
        planServiceAdmin.edit();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity get() {
        planServiceAdmin.get();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity getById() {
        planServiceAdmin.getById();
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete() {
        planServiceAdmin.delete();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
