package com.temm.skillify.model.dto.request;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EssayCreateDTO {
    private String theme;
    private String description;
    private Integer minWords;
    private LocalDateTime maxDate;
    private String classroomId;
}
