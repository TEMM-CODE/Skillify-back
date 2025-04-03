package com.temm.skillify.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.temm.skillify.model.entity.CourseCategory;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.enums.UserRole;
import com.temm.skillify.model.dto.request.CourseCategoryCreateDTO;
import com.temm.skillify.model.dto.response.CourseCategoryResponseDTO;
import com.temm.skillify.model.dto.response.UserResponseDTO;
import com.temm.skillify.repository.CourseCategoryRepository;
import com.temm.skillify.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseCategoryAdminService {
    private final CourseCategoryRepository courseCategoryRepository;
    private final UserRepository userRepository;
    
    public List<CourseCategoryResponseDTO> findAllByCurrentUser() {
        User currentUser = getCurrentUser();
        return courseCategoryRepository.findByCreatedBy(currentUser).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    public Optional<CourseCategoryResponseDTO> findByIdAndCurrentUser(String id) {
        User currentUser = getCurrentUser();
        return courseCategoryRepository.findByIdAndCreatedBy(id, currentUser)
            .map(this::mapToDTO);
    }
    
    public Optional<CourseCategoryResponseDTO> findByCategoryNameAndCurrentUser(String categoryName) {
        User currentUser = getCurrentUser();
        return courseCategoryRepository.findByCategoryNameAndCreatedBy(categoryName, currentUser)
            .map(this::mapToDTO);
    }
    
    public CourseCategoryResponseDTO save(CourseCategoryCreateDTO categoryDTO) {
        User currentUser = getCurrentUser();
        validateAdminRole(currentUser);
        
        CourseCategory category = new CourseCategory();
        category.setCategoryName(categoryDTO.getCategoryName());
        category.setCreatedBy(currentUser);
        
        CourseCategory savedCategory = courseCategoryRepository.save(category);
        return mapToDTO(savedCategory);
    }
    
    public Optional<CourseCategoryResponseDTO> update(String id, CourseCategoryCreateDTO categoryDTO) {
        User currentUser = getCurrentUser();
        validateAdminRole(currentUser);
        
        return courseCategoryRepository.findByIdAndCreatedBy(id, currentUser)
            .map(existingCategory -> {
                existingCategory.setCategoryName(categoryDTO.getCategoryName());
                CourseCategory updatedCategory = courseCategoryRepository.save(existingCategory);
                return mapToDTO(updatedCategory);
            });
    }
    
    public void deleteById(String id) {
        User currentUser = getCurrentUser();
        validateAdminRole(currentUser);
        
        courseCategoryRepository.findByIdAndCreatedBy(id, currentUser)
            .ifPresent(courseCategoryRepository::delete);
    }
    
    private CourseCategoryResponseDTO mapToDTO(CourseCategory entity) {
        CourseCategoryResponseDTO dto = new CourseCategoryResponseDTO();
        dto.setId(entity.getId());
        dto.setCategoryName(entity.getCategoryName());
        
        UserResponseDTO userDTO = new UserResponseDTO();
        userDTO.setId(entity.getCreatedBy().getId());
        userDTO.setEmail(entity.getCreatedBy().getEmail());
        // Set other user fields as needed
        
        dto.setUser(userDTO);
        return dto;
    }
    
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByEmail(username)
            .orElseThrow(() -> new SecurityException("User not found"));
    }

    private void validateAdminRole(User user) {
        if (!user.getRole().equals(UserRole.ADMIN)) {
            throw new SecurityException("Only admins can perform this action");
        }
    }
}