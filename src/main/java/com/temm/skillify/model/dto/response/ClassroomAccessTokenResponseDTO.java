package com.temm.skillify.model.dto.response;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ClassroomAccessTokenResponseDTO extends BaseResponseDTO {
    private ClassroomResponseDTO classroom;
    private String token;
}
