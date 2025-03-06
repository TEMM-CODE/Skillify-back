package com.temm.skillify.model.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.temm.skillify.model.enums.SessionType;

@Data
@EqualsAndHashCode(callSuper = true)
public class TutorSessionResponseDTO extends BaseResponseDTO {
    private UserResponseDTO mentor;
    private String title;
    private LocalDate date;
    private LocalDateTime dateHour;
    private SessionType type;
    private String link;
    private UserResponseDTO student;
}
