package com.temm.skillify.model.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;  // Changed from Set to List
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class ClassroomWithCoursesResponseDTO extends BaseResponseDTO {
    private String name;
    private Set<UserResponseDTO> students;
    private UserResponseDTO mentor;
    private List<ClassroomCourseReturnDTO> courses;  // Changed from Set<String> courseIds
}