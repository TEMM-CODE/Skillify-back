package com.temm.skillify.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MentorProgressStudent {
    private UserResponseDTO user;
    private Integer sequence;
    private Integer initiatedCourses;
}