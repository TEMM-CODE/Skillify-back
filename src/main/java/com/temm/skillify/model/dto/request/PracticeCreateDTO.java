package com.temm.skillify.model.dto.request;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class PracticeCreateDTO {
    private String mentorId;
    private String classroomId;
    private String title;
    private Integer numberOfQuestions;
    private Integer duracao;
    private LocalDateTime openingDate;
    private LocalDateTime maximumDate;
    private Set<String> questionIds;
}
