package com.temm.skillify.model.dto.request;

import com.temm.skillify.model.enums.QuestionContentType;

import lombok.Data;

@Data
public class QuestionContentCreateDTO {
    private String questionId;  
    private int position;
    private QuestionContentType type;
    private String value;
}