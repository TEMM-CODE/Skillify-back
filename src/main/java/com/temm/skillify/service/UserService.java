package com.temm.skillify.service;


import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.temm.skillify.model.entity.User;
import com.temm.skillify.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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