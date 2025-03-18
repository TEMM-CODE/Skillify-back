package com.temm.skillify.model.dto.request;


import com.temm.skillify.model.enums.GoalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalRequestDTO {
    private List<String> classroomIds;
    private Integer number;
    private GoalType type;
    private LocalDateTime openingDate;
    private LocalDateTime finalDate;
}