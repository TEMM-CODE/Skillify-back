package com.temm.skillify.model.dto.request;

import lombok.Data;

@Data
public class CourseLessonContentWatchEventRequestDTO {
    private String courseLessonContentId;
    private String studentId;
}