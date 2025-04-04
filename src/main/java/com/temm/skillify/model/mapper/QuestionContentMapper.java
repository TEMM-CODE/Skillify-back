package com.temm.skillify.model.mapper;



import com.temm.skillify.model.dto.request.QuestionContentCreateDTO;
import com.temm.skillify.model.dto.response.QuestionContentReturnDTO;
import com.temm.skillify.model.dto.response.QuestionResponseDTO;
import com.temm.skillify.model.entity.Question;
import com.temm.skillify.model.entity.QuestionContent;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionContentMapper {
    

    private final UserMapper userMapper;
    private final OptionMapper optionMapper;
    private final CourseMapper courseMapper;

    

    // Convert Entity to Response DTO
    public QuestionContentReturnDTO toResponseDTO(QuestionContent entity) {
        if (entity == null) {
            return null;
        }

        QuestionContentReturnDTO dto = new QuestionContentReturnDTO();
        dto.setId(entity.getId()); // From BaseResponseDTO
        dto.setCreatedAt(entity.getCreatedAt()); // From BaseResponseDTO
        dto.setUpdatedAt(entity.getUpdatedAt()); // From BaseResponseDTO
        dto.setPosition(entity.getPosition());
        dto.setType(entity.getType());
        dto.setValue(entity.getValue());

        return dto;
    }

    // Convert Create DTO to Entity
    public QuestionContent toEntity(QuestionContentCreateDTO dto, Question question) {
        if (dto == null) {
            return null;
        }

        QuestionContent entity = new QuestionContent();
        entity.setPosition(dto.getPosition());
        entity.setType(dto.getType());
        entity.setValue(dto.getValue());
        entity.setQuestion(question);

        return entity;
    }
}