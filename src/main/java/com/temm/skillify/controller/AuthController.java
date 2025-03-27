package com.temm.skillify.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.temm.skillify.model.dto.RegisterRequest;
import com.temm.skillify.model.dto.request.AuthenticationRequest;
import com.temm.skillify.model.dto.response.AuthenticationResponse;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.service.AuthenticationService;
import com.temm.skillify.service.ClassroomAccessTokenService;
import com.temm.skillify.service.ClassroomService;
import com.temm.skillify.service.UserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final ClassroomAccessTokenService classroomAccessTokenService;
    private final UserService userService;
    private final ClassroomService classroomService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/register-with-classroom")
    public ResponseEntity<AuthenticationResponse> registerWithClassroom(
            @RequestBody RegisterRequest request,
            @RequestParam("classId") String classId
    ) {
        // Step 1: Validate the classroom token
        return classroomAccessTokenService.findByToken(classId)
                .map(tokenDTO -> {
                    // Step 2: Register the user
                    AuthenticationResponse authResponse = authenticationService.register(request);

                    // Step 3: Find the user (assuming AuthenticationService stores the user and returns email or ID)
                    User student = userService.findByEmail(request.getEmail())
                            .orElseThrow(() -> new RuntimeException("User not found after registration"));

                    // Step 4: Find the classroom and add the student
                    String classroomId = tokenDTO.getClassroom().getId();
                    return classroomService.findById(classroomId)
                            .map(classroom -> {
                                classroom.getStudents().add(student);
                                classroomService.save(classroom); // Save the updated classroom
                                return ResponseEntity.ok(authResponse); // Return the token
                            })
                            .orElseGet(() -> ResponseEntity.status(404).build()); // Classroom not found
                })
                .orElseGet(() -> ResponseEntity.badRequest().body(null)); // Invalid token
    }
}
