package com.temm.skillify.model.dto.response;


import lombok.Data;
import java.time.LocalDateTime;

@Data
public abstract class BaseResponseDTO {
    private String id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
