package com.temm.skillify.model.dto.response;


import com.temm.skillify.model.enums.GoalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalResponseDTO {
    private String id;
    private List<ClassroomResponseDTO> classrooms;
    private Integer number;
    private GoalType type;
    private LocalDateTime openingDate;
    private LocalDateTime finalDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}