package com.temm.skillify.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.temm.skillify.model.entity.CourseCategory;
import com.temm.skillify.model.dto.request.CourseCategoryCreateDTO;
import com.temm.skillify.model.dto.response.CourseCategoryResponseDTO;
import com.temm.skillify.repository.CourseCategoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseCategoryAdminService {
    private final CourseCategoryRepository courseCategoryRepository;
    
    public List<CourseCategoryResponseDTO> findAll() {
        return courseCategoryRepository.findAll().stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    public Optional<CourseCategoryResponseDTO> findById(String id) {
        return courseCategoryRepository.findById(id)
            .map(this::mapToDTO);
    }
    
    public Optional<CourseCategoryResponseDTO> findByCategoryName(String categoryName) {
        return courseCategoryRepository.findByCategoryName(categoryName)
            .map(this::mapToDTO);
    }
    
    public CourseCategoryResponseDTO save(CourseCategoryCreateDTO categoryDTO) {
        CourseCategory category = new CourseCategory();
        category.setCategoryName(categoryDTO.getCategoryName());
        
        CourseCategory savedCategory = courseCategoryRepository.save(category);
        return mapToDTO(savedCategory);
    }
    
    public Optional<CourseCategoryResponseDTO> update(String id, CourseCategoryCreateDTO categoryDTO) {
        return courseCategoryRepository.findById(id)
            .map(existingCategory -> {
                existingCategory.setCategoryName(categoryDTO.getCategoryName());
                CourseCategory updatedCategory = courseCategoryRepository.save(existingCategory);
                return mapToDTO(updatedCategory);
            });
    }
    
    public void deleteById(String id) {
        courseCategoryRepository.deleteById(id);
    }
    
    private CourseCategoryResponseDTO mapToDTO(CourseCategory entity) {
        CourseCategoryResponseDTO dto = new CourseCategoryResponseDTO();
        dto.setCategoryName(entity.getCategoryName());
        dto.setId(entity.getId());
        // If BaseResponseDTO has fields that need to be set (like id), set them here
        // For example, assuming BaseResponseDTO has an id field:
        // dto.setId(entity.getId());
        return dto;
    }
}