package com.temm.skillify.model.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GoalExecutionResponseDTO extends BaseResponseDTO {
    private GoalResponseDTO goal;
    private UserResponseDTO student;
    private Integer amount;
}