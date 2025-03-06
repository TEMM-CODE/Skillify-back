package com.temm.skillify.model.dto.request;

import lombok.Data;
import java.util.Set;

@Data
public class ClassroomCreateDTO {
    private String name;
    private Set<String> studentIds;
    private String mentorId;
}
