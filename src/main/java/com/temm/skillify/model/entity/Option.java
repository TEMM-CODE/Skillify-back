// Option.java
package com.temm.skillify.model.entity;

import com.temm.skillify.model.categories.BaseEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true, exclude = {"question"}) // Exclude question from hashCode/equals
@NoArgsConstructor
@AllArgsConstructor
public class Option extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.EAGER)
    private Question question;
    
    private String title;
    private Boolean correct;
}