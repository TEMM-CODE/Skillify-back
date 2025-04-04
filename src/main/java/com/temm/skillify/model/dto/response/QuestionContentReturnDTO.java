package com.temm.skillify.model.dto.response;


import lombok.Data;
import lombok.EqualsAndHashCode;
import com.temm.skillify.model.enums.QuestionContentType;

// Return DTO
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionContentReturnDTO extends BaseResponseDTO {
    private int position;
    private QuestionContentType type;
    private String value;
}