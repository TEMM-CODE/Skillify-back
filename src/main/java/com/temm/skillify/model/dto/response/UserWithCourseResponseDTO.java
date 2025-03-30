package com.temm.skillify.model.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

import com.temm.skillify.model.enums.UserRole;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserWithCourseResponseDTO extends UserResponseDTO{
    private List<String> courseNames;
}
