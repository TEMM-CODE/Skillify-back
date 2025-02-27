package com.temm.skillify.controller;

import com.temm.skillify.dto.UserRequestDto;
import com.temm.skillify.dto.UserResponseDto;
import com.temm.skillify.service.UserServiceStudent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userStudent")
public class UserControllerStudent {

    @Autowired
    private UserServiceStudent userServiceStudent;

    @PostMapping
    public ResponseEntity<UserResponseDto> create(@RequestBody UserRequestDto dto) {
        UserResponseDto createdUser = userServiceStudent.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> edit(@PathVariable String id, @RequestBody UserRequestDto dto) {
        UserResponseDto updatedUser = userServiceStudent.edit(id, dto);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAll() {
        return ResponseEntity.ok(userServiceStudent.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(userServiceStudent.getUserById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        return ResponseEntity.noContent().build();
    }
}
