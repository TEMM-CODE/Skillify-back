package com.temm.skillify.model.entity;
import com.temm.skillify.model.categories.BaseEntity;
import com.temm.skillify.model.enums.CourseLessonContentType;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class CourseLessonContent extends BaseEntity {

    @ManyToOne
    @JoinColumn(nullable = false)
    private CourseLesson courseLesson;

    private int position; // Determines the order of elements

    @Enumerated(EnumType.STRING)
    private CourseLessonContentType type; // "TEXT" or "IMAGE"


    @Column(name = "\"value\"", length = 4000) 
    private String value; // Holds either text or an image URL
}
