package com.temm.skillify.service;

import com.temm.skillify.model.dto.request.CourseLessonContentWatchEventRequestDTO;
import com.temm.skillify.model.dto.response.CourseLessonContentWatchEventResponseDTO;
import com.temm.skillify.model.entity.CourseLessonContentWatchEvent;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.mapper.CourseLessonContentWatchEventMapper;
import com.temm.skillify.repository.CourseLessonContentWatchEventRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseLessonContentWatchEventStudentService {

    private final CourseLessonContentWatchEventRepository watchEventRepository;
    private final UserService userService;
    private final CourseLessonContentWatchEventMapper watchEventMapper;

    public List<CourseLessonContentWatchEventResponseDTO> getAllWatchEventsForStudent(Authentication authentication) {
        User student = userService.getUserFromAuthentication(authentication);

        List<CourseLessonContentWatchEvent> watchEvents = watchEventRepository.findByStudent(student);

        return watchEvents.stream()
                .map(watchEventMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public CourseLessonContentWatchEventResponseDTO getWatchEventById(String id, Authentication authentication) {
        User student = userService.getUserFromAuthentication(authentication);

        CourseLessonContentWatchEvent watchEvent = watchEventRepository.findByIdAndStudent(id, student)
                .orElseThrow(() -> new EntityNotFoundException("Watch event not found with id: " + id));

        return watchEventMapper.toResponseDTO(watchEvent);
    }

    public CourseLessonContentWatchEventResponseDTO createWatchEvent(CourseLessonContentWatchEventRequestDTO requestDTO, Authentication authentication) {
        User student = userService.getUserFromAuthentication(authentication);

        // Ensure the student ID in the request matches the authenticated user
        if (!requestDTO.getStudentId().equals(student.getId())) {
            throw new IllegalArgumentException("Student ID in request does not match authenticated user");
        }

        CourseLessonContentWatchEvent watchEvent = watchEventMapper.toEntity(requestDTO);
        watchEvent = watchEventRepository.save(watchEvent);

        return watchEventMapper.toResponseDTO(watchEvent);
    }
}