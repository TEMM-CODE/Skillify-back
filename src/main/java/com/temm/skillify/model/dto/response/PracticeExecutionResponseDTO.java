package com.temm.skillify.model.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class PracticeExecutionResponseDTO extends BaseResponseDTO {
    private UserResponseDTO student;
    private PracticeResponseDTO practice;
    private Set<OptionResponseDTO> selectedAnswers;
    private Long correctAnswers;
    private Long duration;  // Added duration field
}