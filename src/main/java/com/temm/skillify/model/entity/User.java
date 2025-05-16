package com.temm.skillify.model.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.temm.skillify.model.categories.BaseEntity;
import com.temm.skillify.model.enums.UserRole;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_user")
public class User extends BaseEntity implements UserDetails {
    
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    private String tel;
    private String expertise;
    private String biography;
    private String password;
    
    private boolean emailNotifications;
    private boolean pushNotifications;
    private boolean weeklyReport;
    private boolean studyReminder;
    
    @Enumerated(EnumType.STRING)
    private UserRole role;

    private int level;
    private int xp;


    @ElementCollection
    @CollectionTable(name = "horarios_disponiveis", joinColumns = @JoinColumn(name = "profissional_id"))
    @Column(name = "horario")
    private List<LocalTime> horariosDisponiveis = new ArrayList<>();
    
    // Fields required for UserDetails implementation
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
    
    @Override
    public String getUsername() {
        return this.email;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
