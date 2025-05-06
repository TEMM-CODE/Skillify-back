package com.temm.skillify.model.dto.request;

import lombok.Data;

@Data
public class GoalExecutionCreateDTO {
    private String goalId;
    private String studentId;
    private Integer amount;
}