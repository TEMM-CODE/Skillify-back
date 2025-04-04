// Question.java
package com.temm.skillify.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.temm.skillify.model.categories.BaseEntity;
import com.temm.skillify.model.enums.QuestionSuperAdminType;

@Entity
@Data
@EqualsAndHashCode(callSuper = true, exclude = {"options"}) // Exclude options from hashCode/equals
@NoArgsConstructor
@AllArgsConstructor
public class Question extends BaseEntity {
    
    private String title;
    
    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Option> options;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private User mentor;

    @ManyToOne(fetch = FetchType.EAGER)
    private Course course;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("position ASC") // Ensures ordering
    private List<QuestionContent> content = new ArrayList<>();

    @ElementCollection(targetClass = QuestionSuperAdminType.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "question_super_admin_types", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "super_admin_type")
    private List<QuestionSuperAdminType> superAdminTypes;
}