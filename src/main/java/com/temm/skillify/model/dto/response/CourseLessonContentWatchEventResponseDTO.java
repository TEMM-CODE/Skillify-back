package com.temm.skillify.model.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CourseLessonContentWatchEventResponseDTO {
    private String id;
    private String courseLessonContentId;
    private String studentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}