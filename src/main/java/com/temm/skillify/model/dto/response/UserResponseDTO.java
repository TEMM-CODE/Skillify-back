package com.temm.skillify.model.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

import com.temm.skillify.model.enums.UserRole;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserResponseDTO extends BaseResponseDTO {
    private String name;
    private String email;
    private String tel;
    private String biography;
    private String expertise;
    private boolean emailNotifications;
    private boolean pushNotifications;
    private boolean weeklyReport;
    private boolean studyReminder;
    private UserRole role;
    private String avatar;
    private int level;
    private int xp;
    private List<String> horarios;
}