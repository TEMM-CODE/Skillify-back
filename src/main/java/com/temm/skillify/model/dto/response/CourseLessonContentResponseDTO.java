package com.temm.skillify.model.dto.response;


import com.temm.skillify.model.enums.CourseLessonContentType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CourseLessonContentResponseDTO extends BaseResponseDTO {
    private CourseLessonResponseDTO courseLesson;
    private int position;
    private CourseLessonContentType type;
    private String value;
}