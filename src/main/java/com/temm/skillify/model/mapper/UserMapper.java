package com.temm.skillify.model.mapper;

import com.temm.skillify.model.dto.response.UserResponseDTO;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.entity.UserAvatar;
import com.temm.skillify.repository.UserAvatarRepository;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {


    private final UserAvatarRepository userAvatarRepository;

    public UserResponseDTO toResponseDTO(User user) {
        if (user == null) {
            return null;
        }
        
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setTel(user.getTel());
        dto.setBiography(user.getBiography());
        dto.setEmailNotifications(user.isEmailNotifications());
        dto.setPushNotifications(user.isPushNotifications());
        dto.setWeeklyReport(user.isWeeklyReport());
        dto.setStudyReminder(user.isStudyReminder());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        dto.setXp(user.getXp());
        dto.setLevel(user.getLevel());
        dto.setExpertise(user.getExpertise());
        Optional<UserAvatar> avatar = userAvatarRepository.findByUserId(user.getId());
        if(avatar.isPresent()){dto.setAvatar(avatar.get().getImageUrl());}
        
        return dto;
    }
}