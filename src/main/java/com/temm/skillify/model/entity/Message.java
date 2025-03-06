package com.temm.skillify.model.entity;

import com.temm.skillify.model.categories.BaseEntity;

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
public class Message extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.EAGER)
    private User remetente;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private User destinatario;
    
    @Column(length = 4000)
    private String content;
}