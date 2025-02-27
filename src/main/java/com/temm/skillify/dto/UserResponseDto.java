package com.temm.skillify.dto;

import com.temm.skillify.enums.TypeUser;
import com.temm.skillify.model.entity.UserEntity;

import java.time.LocalDateTime;

public record UserResponseDto(
        String id,
        String name,
        String email,
        String phone,
        String biography,
        TypeUser typeUser,
        Boolean emailNotification,
        Boolean pushNotifications,
        Boolean weekReport,
        Boolean studyReminder,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public UserResponseDto(UserEntity user) {
        this(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getBiography(),
                user.getTypeUser(),
                user.getEmailNotification(),
                user.getPushNotifications(),
                user.getWeekReport(),
                user.getStudyReminder(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}