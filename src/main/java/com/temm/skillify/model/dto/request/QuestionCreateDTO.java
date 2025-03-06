package com.temm.skillify.model.dto.request;

import lombok.Data;

@Data
public class QuestionCreateDTO {
    private String title;
    private String mentorId;
}
