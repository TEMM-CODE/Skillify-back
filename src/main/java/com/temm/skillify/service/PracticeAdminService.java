package com.temm.skillify.service;

import com.temm.skillify.model.entity.Option;
import com.temm.skillify.model.entity.Practice;
import com.temm.skillify.model.entity.Question;
import com.temm.skillify.repository.OptionRepository;
import com.temm.skillify.repository.PracticeRepository;
import com.temm.skillify.repository.QuestionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PracticeAdminService {
    
    private final PracticeRepository practiceRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;
    
    public List<Practice> findAllPractices() {
        return practiceRepository.findAll();
    }
    
    public Optional<Practice> findPracticeById(String id) {
        return practiceRepository.findById(id);
    }
    
    public Practice createPractice(Practice practice) {
        return practiceRepository.save(practice);
    }
    
    public Practice updatePractice(String id, Practice updatedPractice) {
        Practice existingPractice = practiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Practice not found with id: " + id));
        
        existingPractice.setTitle(updatedPractice.getTitle());
        existingPractice.setNumberOfQuestions(updatedPractice.getNumberOfQuestions());
        existingPractice.setDuracao(updatedPractice.getDuracao());
        existingPractice.setOpeningDate(updatedPractice.getOpeningDate());
        existingPractice.setMaximumDate(updatedPractice.getMaximumDate());
        
        if (updatedPractice.getMentor() != null) {
            existingPractice.setMentor(updatedPractice.getMentor());
        }
        
        if (updatedPractice.getClassroom() != null) {
            existingPractice.setClassroom(updatedPractice.getClassroom());
        }
        
        if (updatedPractice.getQuestions() != null) {
            existingPractice.setQuestions(updatedPractice.getQuestions());
        }
        
        return practiceRepository.save(existingPractice);
    }
    
    public void deletePractice(String id) {
        practiceRepository.deleteById(id);
    }
    
    public List<Question> findAllQuestions() {
        return questionRepository.findAll();
    }
    
    public Optional<Question> findQuestionById(String id) {
        return questionRepository.findById(id);
    }
    
    public Question createQuestion(Question question) {
        return questionRepository.save(question);
    }
    
    public Question updateQuestion(String id, Question updatedQuestion) {
        Question existingQuestion = questionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Question not found with id: " + id));
        
        existingQuestion.setTitle(updatedQuestion.getTitle());
        
        if (updatedQuestion.getMentor() != null) {
            existingQuestion.setMentor(updatedQuestion.getMentor());
        }
        
        return questionRepository.save(existingQuestion);
    }
    
    public void deleteQuestion(String id) {
        questionRepository.deleteById(id);
    }
    
    public Option addOptionToQuestion(String questionId, Option option) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question not found with id: " + questionId));
        
        option.setQuestion(question);
        return optionRepository.save(option);
    }
    
    public void deleteOption(String optionId) {
        optionRepository.deleteById(optionId);
    }
    
    public Practice updatePracticeQuestions(String practiceId, Set<Question> questions) {
        Practice practice = practiceRepository.findById(practiceId)
                .orElseThrow(() -> new EntityNotFoundException("Practice not found with id: " + practiceId));
        
        practice.setQuestions(questions);
        return practiceRepository.save(practice);
    }
}