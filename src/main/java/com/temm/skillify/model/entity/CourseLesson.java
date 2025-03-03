package com.temm.skillify.model.entity;


import com.temm.skillify.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class CourseLesson extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.EAGER)
    private Course course;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private CourseLessonCategory courseLessonCategory;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private Classroom classroom;
    
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> files;
    
    private String name;
    private Integer duration; // in minutes
}