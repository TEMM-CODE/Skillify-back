package com.temm.skillify.model.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class CourseLessonCreateDTO {
    private String courseId;
    private String courseLessonCategoryId;
    private String classroomId;
    private List<String> files;
    private String name;
    private Integer duration;
}
