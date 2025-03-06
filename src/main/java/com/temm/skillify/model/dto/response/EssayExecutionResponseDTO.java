package com.temm.skillify.model.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class EssayExecutionResponseDTO extends BaseResponseDTO {
    private UserResponseDTO student;
    private EssayResponseDTO essay;
    private String text;
}