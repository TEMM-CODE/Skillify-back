package com.temm.skillify.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String tel;
    private String biography;
    private boolean emailNotifications;
    private boolean pushNotifications;
    private boolean weeklyReport;
    private boolean studyReminder;
    private String role;
}