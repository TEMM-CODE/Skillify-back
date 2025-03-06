package com.temm.skillify.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.temm.skillify.model.dto.RegisterRequest;
import com.temm.skillify.model.dto.request.AuthenticationRequest;
import com.temm.skillify.model.dto.response.AuthenticationResponse;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.enums.UserRole;
import com.temm.skillify.repository.UserRepository;
import com.temm.skillify.security.JwtService;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
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

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}