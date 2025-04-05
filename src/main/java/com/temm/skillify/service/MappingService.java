package com.temm.skillify.service;


import com.temm.skillify.model.dto.response.*;
import com.temm.skillify.model.entity.*;
import com.temm.skillify.model.mapper.CourseMapper;
import com.temm.skillify.model.mapper.QuestionContentMapper;
import com.temm.skillify.model.mapper.QuestionMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MappingService {

    @Autowired
    private QuestionContentMapper questionContentMapper; 

        private final QuestionMapper questionMapper;
    private final CourseMapper courseMapper; 
    
    public UserResponseDTO mapToUserResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setTel(user.getTel());
        dto.setBiography(user.getBiography());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }
    
    public ClassroomResponseDTO mapToClassroomResponseDTO(Classroom classroom) {
        ClassroomResponseDTO dto = new ClassroomResponseDTO();
        dto.setId(classroom.getId());
        dto.setName(classroom.getName());
        dto.setMentor(mapToUserResponseDTO(classroom.getMentor()));
        dto.setCreatedAt(classroom.getCreatedAt());
        dto.setUpdatedAt(classroom.getUpdatedAt());
        return dto;
    }
    
    public OptionResponseDTO mapToOptionResponseDTO(Option option) {
        OptionResponseDTO dto = new OptionResponseDTO();
        dto.setId(option.getId());
        dto.setTitle(option.getTitle());
        dto.setCorrect(option.getCorrect());
        dto.setCreatedAt(option.getCreatedAt());
        dto.setUpdatedAt(option.getUpdatedAt());
        return dto;
    }
    
    public QuestionResponseDTO mapToQuestionResponseDTO(Question question) {
        QuestionResponseDTO dto = new QuestionResponseDTO();
        dto.setId(question.getId());
        dto.setTitle(question.getTitle());
        
        Set<OptionResponseDTO> optionDTOs = question.getOptions().stream()
                .map(this::mapToOptionResponseDTO)
                .collect(Collectors.toSet());
        
        dto.setOptions(optionDTOs);
        dto.setMentor(mapToUserResponseDTO(question.getMentor()));
        dto.setCreatedAt(question.getCreatedAt());
        dto.setUpdatedAt(question.getUpdatedAt());
        if (question.getContent() != null && !question.getContent().isEmpty()) {
            dto.setContent(question.getContent().stream()
                    .map(questionContentMapper::toResponseDTO)
                    .collect(Collectors.toList()));
        } else {
            dto.setContent(new ArrayList<>());
        }
        return dto;
    }
    
    public PracticeResponseDTO mapToPracticeResponseDTO(Practice practice) {
        PracticeResponseDTO dto = new PracticeResponseDTO();
        dto.setId(practice.getId());
        dto.setTitle(practice.getTitle());
        dto.setMentor(mapToUserResponseDTO(practice.getMentor()));
        dto.setClassroom(mapToClassroomResponseDTO(practice.getClassroom()));
        dto.setNumberOfQuestions(practice.getNumberOfQuestions());
        dto.setDuracao(practice.getDuracao());
        dto.setOpeningDate(practice.getOpeningDate());
        dto.setMaximumDate(practice.getMaximumDate());
        
        if (practice.getCourses() != null && !practice.getCourses().isEmpty()) {
            dto.setCourses(practice.getCourses().stream()
                .map(courseMapper::toResponseDTO)
                .collect(Collectors.toList()));
        }
        
        if (practice.getQuestions() != null && !practice.getQuestions().isEmpty()) {
            dto.setQuestions(practice.getQuestions().stream()
                .map(questionMapper::toResponseDTO)
                .collect(Collectors.toSet()));
        }
        dto.setCreatedAt(practice.getCreatedAt());
        dto.setUpdatedAt(practice.getUpdatedAt());
        return dto;
    }
    
    public EssayResponseDTO mapToEssayResponseDTO(Essay essay) {
        EssayResponseDTO dto = new EssayResponseDTO();
        dto.setId(essay.getId());
        dto.setTheme(essay.getTheme());
        dto.setDescription(essay.getDescription());
        dto.setMinWords(essay.getMinWords());
        dto.setMaxDate(essay.getMaxDate());
        dto.setClassroom(mapToClassroomResponseDTO(essay.getClassroom()));
        dto.setCreatedAt(essay.getCreatedAt());
        dto.setUpdatedAt(essay.getUpdatedAt());
        return dto;
    }
    
    public EssayExecutionResponseDTO mapToEssayExecutionResponseDTO(EssayExecution execution) {
        EssayExecutionResponseDTO dto = new EssayExecutionResponseDTO();
        dto.setId(execution.getId());
        dto.setStudent(mapToUserResponseDTO(execution.getStudent()));
        dto.setEssay(mapToEssayResponseDTO(execution.getEssay()));
        dto.setText(execution.getText());
        dto.setCreatedAt(execution.getCreatedAt());
        dto.setUpdatedAt(execution.getUpdatedAt());
        return dto;
    }
    
    public EssayCorrectionResponseDTO mapToEssayCorrectionResponseDTO(EssayCorrection correction) {
        EssayCorrectionResponseDTO dto = new EssayCorrectionResponseDTO();
        dto.setId(correction.getId());
        dto.setEssay(mapToEssayResponseDTO(correction.getEssay()));
        dto.setMentor(mapToUserResponseDTO(correction.getMentor()));
        dto.setEssayExecution(mapToEssayExecutionResponseDTO(correction.getEssayExecution()));
        dto.setEstruturaCoesaoComentario(correction.getEstruturaCoesaoComentario());
        dto.setArgumentacaoComentario(correction.getArgumentacaoComentario());
        dto.setConquistas(correction.getConquistas());
        dto.setCompetencia1Score(correction.getCompetencia1Score());
        dto.setCompetencia2Score(correction.getCompetencia2Score());
        dto.setCompetencia3Score(correction.getCompetencia3Score());
        dto.setCompetencia4Score(correction.getCompetencia4Score());
        dto.setCompetencia5Score(correction.getCompetencia5Score());
        dto.setCreatedAt(correction.getCreatedAt());
        dto.setUpdatedAt(correction.getUpdatedAt());
        return dto;
    }
}