package com.temm.skillify.model.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CourseLessonResponseDTO extends BaseResponseDTO {
    private CourseResponseDTO course;
    private CourseLessonCategoryResponseDTO courseLessonCategory;
    private ClassroomResponseDTO classroom;
    private List<String> files;
    private String name;
    private Integer duration;
    private List<CourseLessonContentResponseDTO> content; // Added content list
}