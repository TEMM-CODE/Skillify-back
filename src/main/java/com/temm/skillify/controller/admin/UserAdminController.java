package com.temm.skillify.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.temm.skillify.model.dto.RegisterRequest;
import com.temm.skillify.model.dto.response.UserResponseDTO;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.service.UserService;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class UserAdminController {
    private final UserService userService;
    
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.findAllDto());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable String id) {
        return userService.findDtoById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/mentors")
    public ResponseEntity<List<UserResponseDTO>> getAllMentors() {
        return ResponseEntity.ok(userService.findAllMentorsDto());
    }
    
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody RegisterRequest user) {
        return ResponseEntity.ok(userService.createAndReturnDto(user));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable String id, @RequestBody RegisterRequest request) {
        return userService.findById(id)
            .map(existingUser -> {
                existingUser.setBiography(request.getBiography());
                existingUser.setExpertise(request.getExpertise());
                existingUser.setTel(request.getTel());
                existingUser.setName(request.getName());
                existingUser.setEmail(request.getEmail());
                 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
                      List<LocalTime> horarios = request.getHorarios().stream()
                      .map(s -> LocalTime.parse(s, formatter))
                      .collect(Collectors.toList());
        existingUser.setHorariosDisponiveis(horarios);
                return ResponseEntity.ok(userService.saveAndReturnDto(existingUser));
            })
            .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        return userService.findById(id)
            .map(user -> {
                userService.deleteById(id);
                return ResponseEntity.ok().<Void>build();
            })
            .orElse(ResponseEntity.notFound().build());
    }
}