package com.temm.skillify.model.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CourseLessonCategoryResponseDTO extends BaseResponseDTO {
    private CourseResponseDTO course;
    private String name;
}