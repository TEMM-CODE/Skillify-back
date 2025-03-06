package com.temm.skillify.model.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class EssayResponseDTO extends BaseResponseDTO {
    private String theme;
    private String description;
    private Integer minWords;
    private LocalDateTime maxDate;
    private ClassroomResponseDTO classroom;
}
