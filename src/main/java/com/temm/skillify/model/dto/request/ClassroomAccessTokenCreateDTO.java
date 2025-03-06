package com.temm.skillify.model.dto.request;


import lombok.Data;

@Data
public class ClassroomAccessTokenCreateDTO {
    private String classroomId;
    private String token;
}