package com.temm.skillify.model.dto.request;

import lombok.Data;
import java.util.Set;

@Data
public class PracticeExecutionCreateDTO {
    private String studentId;
    private String practiceId;
    private Set<String> selectedAnswerIds;
    private Long correctAnswers;
    private Long duration;  // Added duration field
}