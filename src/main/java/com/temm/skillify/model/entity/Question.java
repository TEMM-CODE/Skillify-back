package com.temm.skillify.model.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

import com.temm.skillify.model.categories.BaseEntity;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Question extends BaseEntity {
    
    private String title;
    
    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Option> options;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private User mentor;
}