package com.temm.skillify.model.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class ClassroomResponseDTO extends BaseResponseDTO {
    private String name;
    private Set<UserResponseDTO> students;
    private UserResponseDTO mentor;
}
