package com.temm.skillify.model.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionResponseDTO extends BaseResponseDTO {
    private String title;
    private Set<OptionResponseDTO> options;
    private UserResponseDTO mentor;
}
