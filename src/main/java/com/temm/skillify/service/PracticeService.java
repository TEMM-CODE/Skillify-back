package com.temm.skillify.service;

import com.temm.skillify.model.entity.Classroom;
import com.temm.skillify.model.entity.Practice;
import com.temm.skillify.model.entity.Question;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.repository.ClassroomRepository;
import com.temm.skillify.repository.PracticeRepository;
import com.temm.skillify.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PracticeService {

    private final PracticeRepository practiceRepository;
    private final QuestionRepository questionRepository;
    private final ClassroomRepository classroomRepository;
    private final UserService userService;

    public List<Practice> findAllByMentor(Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        return practiceRepository.findByMentor(mentor);
    }

    public Practice findByIdAndMentor(String id, Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        return practiceRepository.findById(id)
                .filter(practice -> practice.getMentor().getId().equals(mentor.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Practice not found or you don't have permission"));
    }

    public List<Practice> findByClassroomAndMentor(String classroomId, Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        Classroom classroom = classroomRepository.findByIdAndMentor(classroomId, mentor)
                .orElseThrow(() -> new EntityNotFoundException("Classroom not found or you don't have permission"));
        return practiceRepository.findByClassroom(classroom);
    }

    public Practice create(Practice practice, Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        practice.setMentor(mentor);
        
        if (practice.getClassroom() != null) {
            Classroom classroom = classroomRepository.findByIdAndMentor(practice.getClassroom().getId(), mentor)
                    .orElseThrow(() -> new EntityNotFoundException("Classroom not found or you don't have permission"));
            practice.setClassroom(classroom);
        }
        
        if (practice.getQuestions() == null) {
            practice.setQuestions(new HashSet<>());
        }
        
        return practiceRepository.save(practice);
    }

    public Practice update(String id, Practice updatedPractice, Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        
        Practice practice = practiceRepository.findById(id)
                .filter(p -> p.getMentor().getId().equals(mentor.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Practice not found or you don't have permission"));
        
        practice.setTitle(updatedPractice.getTitle());
        practice.setNumberOfQuestions(updatedPractice.getNumberOfQuestions());
        practice.setDuracao(updatedPractice.getDuracao());
        practice.setOpeningDate(updatedPractice.getOpeningDate());
        practice.setMaximumDate(updatedPractice.getMaximumDate());
        
        if (updatedPractice.getClassroom() != null) {
            Classroom classroom = classroomRepository.findByIdAndMentor(updatedPractice.getClassroom().getId(), mentor)
                    .orElseThrow(() -> new EntityNotFoundException("Classroom not found or you don't have permission"));
            practice.setClassroom(classroom);
        }
        
        return practiceRepository.save(practice);
    }

    public void delete(String id, Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        
        Practice practice = practiceRepository.findById(id)
                .filter(p -> p.getMentor().getId().equals(mentor.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Practice not found or you don't have permission"));
        
        practiceRepository.delete(practice);
    }

    public Practice addQuestion(String practiceId, String questionId, Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        
        Practice practice = practiceRepository.findById(practiceId)
                .filter(p -> p.getMentor().getId().equals(mentor.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Practice not found or you don't have permission"));
        
        Question question = questionRepository.findById(questionId)
                .filter(q -> q.getMentor().getId().equals(mentor.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Question not found or you don't have permission"));
        
        practice.getQuestions().add(question);
        return practiceRepository.save(practice);
    }

    public Practice removeQuestion(String practiceId, String questionId, Authentication authentication) {
        User mentor = userService.getUserFromAuthentication(authentication);
        
        Practice practice = practiceRepository.findById(practiceId)
                .filter(p -> p.getMentor().getId().equals(mentor.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Practice not found or you don't have permission"));
        
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question not found"));
        
        practice.getQuestions().remove(question);
        return practiceRepository.save(practice);
    }
}