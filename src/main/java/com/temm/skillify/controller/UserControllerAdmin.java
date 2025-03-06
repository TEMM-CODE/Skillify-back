package com.temm.skillify.controller;

import com.temm.skillify.dto.UserRequestDto;
import com.temm.skillify.dto.UserResponseDto;
import com.temm.skillify.service.UserServiceAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userAdmin")
public class UserControllerAdmin {

    @Autowired
    private UserServiceAdmin userServiceAdmin;

    @PostMapping
    public ResponseEntity<UserResponseDto> create(@RequestBody UserRequestDto dto) {
        UserResponseDto createdUser = userServiceAdmin.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> edit(@PathVariable String id, @RequestBody UserRequestDto dto) {
        UserResponseDto updatedUser = userServiceAdmin.edit(id, dto);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAll() {
        return ResponseEntity.ok(userServiceAdmin.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(userServiceAdmin.getUserById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        return ResponseEntity.noContent().build();
    }
}
