package com.temm.skillify.service;


import com.temm.skillify.model.dto.request.PracticeCreateDTO;
import com.temm.skillify.model.dto.response.PracticeResponseDTO;
import com.temm.skillify.model.entity.Classroom;
import com.temm.skillify.model.entity.Practice;
import com.temm.skillify.model.entity.Question;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.mapper.PracticeMapper;
import com.temm.skillify.repository.ClassroomRepository;
import com.temm.skillify.repository.PracticeRepository;
import com.temm.skillify.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PracticeService {

    private final PracticeRepository practiceRepository;
    private final QuestionRepository questionRepository;
    private final ClassroomRepository classroomRepository;
    private final UserService userService;
    private final PracticeMapper practiceMapper;

    public List<PracticeResponseDTO> findAllByMentor(Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        List<Practice> practices = practiceRepository.findByMentor(mentor);
        return practices.stream()
                .map(practiceMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public PracticeResponseDTO findByIdAndMentor(String id, Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        Practice practice = practiceRepository.findById(id)
                .filter(p -> p.getMentor().getId().equals(mentor.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Practice not found or you don't have permission"));
        return practiceMapper.toResponseDTO(practice);
    }

    public List<PracticeResponseDTO> findByClassroomAndMentor(String classroomId, Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        Classroom classroom = classroomRepository.findByIdAndMentor(classroomId, mentor)
                .orElseThrow(() -> new EntityNotFoundException("Classroom not found or you don't have permission"));
        List<Practice> practices = practiceRepository.findByClassroom(classroom);
        return practices.stream()
                .map(practiceMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public PracticeResponseDTO create(PracticeCreateDTO practiceDTO, Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        Practice practice = practiceMapper.toEntity(practiceDTO);
        practice.setMentor(mentor);
        
        if (practiceDTO.getClassroomId() != null) {
            Classroom classroom = classroomRepository.findByIdAndMentor(practiceDTO.getClassroomId(), mentor)
                    .orElseThrow(() -> new EntityNotFoundException("Classroom not found or you don't have permission"));
            practice.setClassroom(classroom);
        }
        
        if (practiceDTO.getQuestionIds() != null && !practiceDTO.getQuestionIds().isEmpty()) {
            Set<Question> questions = practiceDTO.getQuestionIds().stream()
                    .map(questionId -> questionRepository.findById(questionId)
                            .filter(q -> q.getMentor().getId().equals(mentor.getId()))
                            .orElseThrow(() -> new EntityNotFoundException("Question not found or you don't have permission: " + questionId)))
                    .collect(Collectors.toSet());
            practice.setQuestions(questions);
        } else {
            practice.setQuestions(new HashSet<>());
        }
        
        Practice savedPractice = practiceRepository.save(practice);
        return practiceMapper.toResponseDTO(savedPractice);
    }

    public PracticeResponseDTO update(String id, PracticeCreateDTO practiceDTO, Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        
        Practice practice = practiceRepository.findById(id)
                .filter(p -> p.getMentor().getId().equals(mentor.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Practice not found or you don't have permission"));
        
        practice.setTitle(practiceDTO.getTitle());
        practice.setNumberOfQuestions(practiceDTO.getNumberOfQuestions());
        practice.setDuracao(practiceDTO.getDuracao());
        practice.setOpeningDate(practiceDTO.getOpeningDate());
        practice.setMaximumDate(practiceDTO.getMaximumDate());
        
        if (practiceDTO.getClassroomId() != null) {
            Classroom classroom = classroomRepository.findByIdAndMentor(practiceDTO.getClassroomId(), mentor)
                    .orElseThrow(() -> new EntityNotFoundException("Classroom not found or you don't have permission"));
            practice.setClassroom(classroom);
        }
        
        if (practiceDTO.getQuestionIds() != null) {
            Set<Question> questions = practiceDTO.getQuestionIds().stream()
                    .map(questionId -> questionRepository.findById(questionId)
                            .filter(q -> q.getMentor().getId().equals(mentor.getId()))
                            .orElseThrow(() -> new EntityNotFoundException("Question not found or you don't have permission: " + questionId)))
                    .collect(Collectors.toSet());
            practice.setQuestions(questions);
        }
        
        Practice updatedPractice = practiceRepository.save(practice);
        return practiceMapper.toResponseDTO(updatedPractice);
    }

    public void delete(String id, Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        
        Practice practice = practiceRepository.findById(id)
                .filter(p -> p.getMentor().getId().equals(mentor.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Practice not found or you don't have permission"));
        
        practiceRepository.delete(practice);
    }

    public PracticeResponseDTO addQuestion(String practiceId, String questionId, Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        
        Practice practice = practiceRepository.findById(practiceId)
                .filter(p -> p.getMentor().getId().equals(mentor.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Practice not found or you don't have permission"));
        
        Question question = questionRepository.findById(questionId)
                .filter(q -> q.getMentor().getId().equals(mentor.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Question not found or you don't have permission"));
        
        practice.getQuestions().add(question);
        Practice updatedPractice = practiceRepository.save(practice);
        return practiceMapper.toResponseDTO(updatedPractice);
    }

    public PracticeResponseDTO removeQuestion(String practiceId, String questionId, Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        
        Practice practice = practiceRepository.findById(practiceId)
                .filter(p -> p.getMentor().getId().equals(mentor.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Practice not found or you don't have permission"));
        
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question not found"));
        
        practice.getQuestions().remove(question);
        Practice updatedPractice = practiceRepository.save(practice);
        return practiceMapper.toResponseDTO(updatedPractice);
    }
}