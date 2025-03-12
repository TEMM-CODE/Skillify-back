package com.temm.skillify.model.mapper;


import com.temm.skillify.model.dto.response.MessageResponseDTO;
import com.temm.skillify.model.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageMapper {
    
    private final UserMapper userMapper;
    
    public MessageResponseDTO toResponseDTO(Message message) {
        if (message == null) {
            return null;
        }
        
        MessageResponseDTO dto = new MessageResponseDTO();
        dto.setId(message.getId());
        dto.setCreatedAt(message.getCreatedAt());
        dto.setUpdatedAt(message.getUpdatedAt());
        dto.setContent(message.getContent());
        
        if (message.getRemetente() != null) {
            dto.setRemetente(userMapper.toResponseDTO(message.getRemetente()));
        }
        
        if (message.getDestinatario() != null) {
            dto.setDestinatario(userMapper.toResponseDTO(message.getDestinatario()));
        }
        
        return dto;
    }
}