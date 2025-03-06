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
public class Classroom extends BaseEntity {
    
    private String name;
    
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<User> students;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private User mentor;
}