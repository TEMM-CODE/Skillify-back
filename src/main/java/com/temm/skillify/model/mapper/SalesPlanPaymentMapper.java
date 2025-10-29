package com.temm.skillify.model.mapper;

import com.temm.skillify.model.dto.request.SalesPlanPaymentCreateDTO;
import com.temm.skillify.model.dto.response.SalesPlanPaymentResponseDTO;
import com.temm.skillify.model.entity.SalesPlanPayment;
import com.temm.skillify.model.entity.SalesPlanMembershipEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SalesPlanPaymentMapper {

    private final SalesPlanMembershipEventMapper membershipEventMapper;

    public SalesPlanPaymentResponseDTO toResponseDTO(SalesPlanPayment payment) {
        if (payment == null) {
            return null;
        }

        SalesPlanPaymentResponseDTO dto = new SalesPlanPaymentResponseDTO();
        dto.setId(payment.getId());
        dto.setStartDate(payment.getStartDate());
        dto.setEndDate(payment.getEndDate());
        dto.setExpiresAt(payment.getExpiresAt());
        dto.setConfirmed(payment.isConfirmed());
        dto.setExpired(payment.isExpired());
        dto.setValue(payment.getValue());
        dto.setSalesPlanMembershipEvent(
                membershipEventMapper.toDTO(payment.getSalesPlanMembershipEvent())
        );
        dto.setCreatedAt(payment.getCreatedAt());
        dto.setUpdatedAt(payment.getUpdatedAt());

        return dto;
    }

    public SalesPlanPayment toEntity(SalesPlanPaymentCreateDTO dto) {
        if (dto == null) {
            return null;
        }

        SalesPlanPayment payment = new SalesPlanPayment();
        payment.setStartDate(dto.getStartDate());
        payment.setEndDate(dto.getEndDate());
        payment.setExpiresAt(dto.getExpiresAt());
        payment.setConfirmed(dto.isConfirmed());
        payment.setExpired(dto.isExpired());
        payment.setValue(dto.getValue());

        if (dto.getSalesPlanMembershipEventId() != null) {
            SalesPlanMembershipEvent event = new SalesPlanMembershipEvent();
            event.setId(dto.getSalesPlanMembershipEventId());
            payment.setSalesPlanMembershipEvent(event);
        }

        return payment;
    }

    public void updateEntityFromDTO(SalesPlanPayment payment, SalesPlanPaymentCreateDTO dto) {
        if (payment == null || dto == null) {
            return;
        }

        payment.setStartDate(dto.getStartDate());
        payment.setEndDate(dto.getEndDate());
        payment.setExpiresAt(dto.getExpiresAt());
        payment.setConfirmed(dto.isConfirmed());
        payment.setExpired(dto.isExpired());
        payment.setValue(dto.getValue());

        if (dto.getSalesPlanMembershipEventId() != null) {
            SalesPlanMembershipEvent event = new SalesPlanMembershipEvent();
            event.setId(dto.getSalesPlanMembershipEventId());
            payment.setSalesPlanMembershipEvent(event);
        }
    }

    public List<SalesPlanPaymentResponseDTO> toResponseDTOList(List<SalesPlanPayment> payments) {
        if (payments == null) {
            return null;
        }
        return payments.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}