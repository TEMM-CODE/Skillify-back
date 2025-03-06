package com.temm.skillify.model.dto.request;

import lombok.Data;
import java.util.Set;

@Data
public class CourseCreateDTO {
    private Set<String> categoryIds;
    private String level;
    private String name;
    private String description;
    private String creatorId;
    private Integer duration;
    private String imageUrl;
}