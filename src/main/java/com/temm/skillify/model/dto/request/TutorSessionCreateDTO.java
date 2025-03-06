package com.temm.skillify.model.dto.request;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.temm.skillify.model.enums.SessionType;

@Data
public class TutorSessionCreateDTO {
    private String mentorId;
    private String title;
    private LocalDate date;
    private LocalDateTime dateHour;
    private SessionType type;
    private String link;
    private String studentId;
}
