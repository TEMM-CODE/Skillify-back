package com.temm.skillify.model.mapper;

import com.temm.skillify.model.dto.request.CourseLessonContentWatchEventRequestDTO;
import com.temm.skillify.model.dto.response.CourseLessonContentWatchEventResponseDTO;
import com.temm.skillify.model.entity.CourseLessonContent;
import com.temm.skillify.model.entity.CourseLessonContentWatchEvent;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.repository.CourseLessonContentRepository;
import com.temm.skillify.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CourseLessonContentWatchEventMapper {

    private final CourseLessonContentRepository courseLessonContentRepository;
    private final UserService userService;

    public CourseLessonContentWatchEventResponseDTO toResponseDTO(CourseLessonContentWatchEvent watchEvent) {
        if (watchEvent == null) {
            return null;
        }

        CourseLessonContentWatchEventResponseDTO dto = new CourseLessonContentWatchEventResponseDTO();
        dto.setId(watchEvent.getId());
        dto.setCourseLessonContentId(watchEvent.getCourseLessonContent().getId());
        dto.setStudentId(watchEvent.getStudent().getId());
        dto.setCreatedAt(watchEvent.getCreatedAt());
        dto.setUpdatedAt(watchEvent.getUpdatedAt());

        return dto;
    }

    public List<CourseLessonContentWatchEventResponseDTO> toResponseDTOList(List<CourseLessonContentWatchEvent> watchEvents) {
        return watchEvents.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public CourseLessonContentWatchEvent toEntity(CourseLessonContentWatchEventRequestDTO createDTO) {
        if (createDTO == null) {
            return null;
        }

        CourseLessonContentWatchEvent watchEvent = new CourseLessonContentWatchEvent();

        // Fetch related entities
        CourseLessonContent courseLessonContent = courseLessonContentRepository.findById(createDTO.getCourseLessonContentId())
                .orElseThrow(() -> new RuntimeException("Course lesson content not found"));
        User student = userService.findById(createDTO.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        watchEvent.setCourseLessonContent(courseLessonContent);
        watchEvent.setStudent(student);

        return watchEvent;
    }

    public void updateEntityFromDTO(CourseLessonContentWatchEvent watchEvent, CourseLessonContentWatchEventRequestDTO updateDTO) {
        if (updateDTO.getCourseLessonContentId() != null) {
            CourseLessonContent courseLessonContent = courseLessonContentRepository.findById(updateDTO.getCourseLessonContentId())
                    .orElseThrow(() -> new RuntimeException("Course lesson content not found"));
            watchEvent.setCourseLessonContent(courseLessonContent);
        }

        if (updateDTO.getStudentId() != null) {
            User student = userService.findById(updateDTO.getStudentId())
                    .orElseThrow(() -> new RuntimeException("Student not found"));
            watchEvent.setStudent(student);
        }
    }
}
