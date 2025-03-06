package com.temm.skillify.service;

import com.temm.skillify.dto.UserRequestDto;
import com.temm.skillify.dto.UserResponseDto;
import com.temm.skillify.enums.TypeUser;
import com.temm.skillify.model.entity.UserEntity;
import com.temm.skillify.model.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceStudent {
    @Autowired
    private UserRepository userRepository;

    public UserResponseDto create (UserRequestDto requestDto) {
        UserEntity userEntity = new UserEntity();
        userEntity = userRepository.save(userEntity);
        return new UserResponseDto(userEntity);
    }

    public UserResponseDto edit(String id, UserRequestDto userRequestDto) {
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("User not found"));

        entity.setName(userRequestDto.name());
        entity.setEmail(userRequestDto.email());
        entity.setPhone(userRequestDto.phone());
        entity.setBiography(userRequestDto.biography());
        entity.setTypeUser(userRequestDto.typeUser());
        entity.setEmailNotification(userRequestDto.emailNotification());
        entity.setPushNotifications(userRequestDto.pushNotifications());
        entity.setWeekReport(userRequestDto.weekReport());
        entity.setStudyReminder(userRequestDto.studyReminder());

        entity = userRepository.save(entity);
        return new UserResponseDto(entity);

    }
    public List<UserResponseDto> getAll() {
        return userRepository.findAll().stream().map(UserResponseDto:: new).toList();
    }

    public UserResponseDto getUserById(String id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return new UserResponseDto(user);
    }

    public List<UserResponseDto> getUsersByTypeUser(TypeUser typeUser) {
        return userRepository.findByTypeUser(typeUser).stream().map(UserResponseDto::new).toList();
    }

    public void delete(String id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        userRepository.delete(user);
    }
}
