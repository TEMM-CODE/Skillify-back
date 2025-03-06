package com.temm.skillify.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.temm.skillify.model.entity.Essay;
import com.temm.skillify.model.entity.EssayCorrection;
import com.temm.skillify.model.entity.EssayExecution;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.repository.EssayCorrectionRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EssayCorrectionAdminService {
    
    private final EssayCorrectionRepository essayCorrectionRepository;
    
    public List<EssayCorrection> findAll() {
        return essayCorrectionRepository.findAll();
    }
    
    public Optional<EssayCorrection> findById(String id) {
        return essayCorrectionRepository.findById(id);
    }
    
    public List<EssayCorrection> findByMentor(User mentor) {
        return essayCorrectionRepository.findByMentor(mentor);
    }
    
    public List<EssayCorrection> findByEssay(Essay essay) {
        return essayCorrectionRepository.findByEssay(essay);
    }
    
    public Optional<EssayCorrection> findByEssayExecution(EssayExecution execution) {
        return essayCorrectionRepository.findByEssayExecution(execution);
    }
    
    public EssayCorrection save(EssayCorrection essayCorrection) {
        return essayCorrectionRepository.save(essayCorrection);
    }
    
    public void deleteById(String id) {
        essayCorrectionRepository.deleteById(id);
    }
}