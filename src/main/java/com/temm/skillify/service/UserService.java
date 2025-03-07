package com.temm.skillify.service;


import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.temm.skillify.model.dto.RegisterRequest;
import com.temm.skillify.model.dto.response.AuthenticationResponse;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.enums.UserRole;
import com.temm.skillify.repository.UserRepository;
import com.temm.skillify.security.JwtService;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public AuthenticationResponse create(RegisterRequest request) {
        var role = UserRole.valueOf(request.getRole());
        var user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setTel(request.getTel());
        user.setBiography(request.getBiography());
        user.setEmailNotifications(request.isEmailNotifications());
        user.setPushNotifications(request.isPushNotifications());
        user.setWeeklyReport(request.isWeeklyReport());
        user.setStudyReminder(request.isStudyReminder());
        user.setRole(role);
        
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }


    public void deleteById(String id) {
        userRepository.deleteById(id);
    }

        public User getUserFromAuthentication(Authentication authentication) {
        // Assuming the authentication principal is the email of the user
        String email = authentication.getName();
        return findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}