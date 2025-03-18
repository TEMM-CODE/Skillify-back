package com.temm.skillify.model.mapper;



import com.temm.skillify.model.dto.response.OrderResponseDTO;
import com.temm.skillify.model.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderResponseDTO toDTO(Order order) {
        if (order == null) {
            return null;
        }
        
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setSalesPlanId(order.getSalesPlan().getId());
        dto.setSalesPlanName(order.getSalesPlan().getName());
        dto.setUserId(order.getUser().getId());
        dto.setUserName(order.getUser().getName());
        dto.setPurchaseDate(order.getPurchaseDate());
        dto.setExpirationDate(order.getExpirationDate());
        dto.setActive(order.isActive());
        dto.setPrice(order.getSalesPlan().getPrice());
        
        return dto;
    }
}