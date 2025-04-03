package com.temm.skillify.model.dto.request;

import java.util.List;

import com.temm.skillify.model.enums.QuestionSuperAdminType;

import lombok.Data;

@Data
public class QuestionCreateDTO {
    private String title;
    private String mentorId;
    private String courseId; // Added courseId
    private List<QuestionSuperAdminType> superAdminTypes;
}