package com.temm.skillify.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.temm.skillify.model.dto.RegisterRequest;
import com.temm.skillify.model.dto.response.AuthenticationResponse;
import com.temm.skillify.model.dto.response.UserResponseDTO;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.enums.UserRole;
import com.temm.skillify.model.mapper.UserMapper;
import com.temm.skillify.repository.UserRepository;
import com.temm.skillify.security.JwtService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    public List<UserResponseDTO> findAllDto() {
        return findAll().stream()
            .map(userMapper::toResponseDTO)
            .collect(Collectors.toList());
    }
    
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }
    
    public Optional<UserResponseDTO> findDtoById(String id) {
        return findById(id).map(userMapper::toResponseDTO);
    }
    
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public Optional<UserResponseDTO> findDtoByEmail(String email) {
        return findByEmail(email).map(userMapper::toResponseDTO);
    }
    
    public User save(User user) {
        return userRepository.save(user);
    }
    
    public UserResponseDTO saveAndReturnDto(User user) {
        return userMapper.toResponseDTO(save(user));
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
    
    public UserResponseDTO createAndReturnDto(RegisterRequest request) {
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
        User savedUser = userRepository.save(user);
        return userMapper.toResponseDTO(savedUser);
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
    
    public UserResponseDTO getUserDtoFromAuthentication(Authentication authentication) {
        return userMapper.toResponseDTO(getUserFromAuthentication(authentication));
    }
}