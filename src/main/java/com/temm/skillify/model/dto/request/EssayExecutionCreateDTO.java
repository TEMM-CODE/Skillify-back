package com.temm.skillify.model.dto.request;

import lombok.Data;

@Data
public class EssayExecutionCreateDTO {
    private String studentId;
    private String essayId;
    private String text;
}