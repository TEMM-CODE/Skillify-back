package com.temm.skillify.model.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OptionResponseDTO extends BaseResponseDTO {
    private String title;
    private Boolean correct;
}