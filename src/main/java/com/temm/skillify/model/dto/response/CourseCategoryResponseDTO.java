package com.temm.skillify.model.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CourseCategoryResponseDTO extends BaseResponseDTO {
    private String categoryName;
}