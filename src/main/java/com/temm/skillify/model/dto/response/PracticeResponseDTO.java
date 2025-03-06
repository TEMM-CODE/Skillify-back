package com.temm.skillify.model.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class PracticeResponseDTO extends BaseResponseDTO {
    private UserResponseDTO mentor;
    private ClassroomResponseDTO classroom;
    private String title;
    private Integer numberOfQuestions;
    private Integer duracao;
    private LocalDateTime openingDate;
    private LocalDateTime maximumDate;
    private Set<QuestionResponseDTO> questions;
}
