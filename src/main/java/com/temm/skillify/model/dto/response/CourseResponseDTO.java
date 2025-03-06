package com.temm.skillify.model.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class CourseResponseDTO extends BaseResponseDTO {
    private Set<CourseCategoryResponseDTO> categories;
    private String level;
    private String name;
    private String description;
    private UserResponseDTO creator;
    private Integer duration;
    private String imageUrl;
}