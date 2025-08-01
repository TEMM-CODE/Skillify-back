package com.temm.skillify.model.dto.request;

import org.springframework.web.multipart.MultipartFile;

import com.temm.skillify.model.enums.CourseLessonContentType;
import lombok.Data;

@Data
public class CourseLessonContentCreateDTO {
    private String courseLessonId;
    private int position;
    private CourseLessonContentType type;
    private String value;
    private MultipartFile videoFile;
}