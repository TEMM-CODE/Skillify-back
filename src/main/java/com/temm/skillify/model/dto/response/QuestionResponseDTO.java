package com.temm.skillify.model.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Set;

import com.temm.skillify.model.enums.QuestionSuperAdminType;

@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionResponseDTO extends BaseResponseDTO {
    private String title;
    private Set<OptionResponseDTO> options;
    private UserResponseDTO mentor;
    private List<QuestionSuperAdminType> superAdminTypes;
}
