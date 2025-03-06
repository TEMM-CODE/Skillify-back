package com.temm.skillify.dto;

import com.temm.skillify.enums.TypeUser;
import com.temm.skillify.model.entity.UserEntity;

public record UserRequestDto(
        String name,
        String email,
        String phone,
        String biography,
        TypeUser typeUser,
        Boolean emailNotification,
        Boolean pushNotifications,
        Boolean weekReport,
        Boolean studyReminder
) {
    public UserRequestDto(UserEntity entity){
        this(
                entity.getName(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getBiography(),
                entity.getTypeUser(),
                entity.getEmailNotification(),
                entity.getPushNotifications(),
                entity.getWeekReport(),
                entity.getStudyReminder()
        );
    }
}
