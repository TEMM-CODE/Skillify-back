package com.temm.skillify.model.mapper;

import com.temm.skillify.model.dto.request.SalesPlanAdminPaymentCreateDTO;
import com.temm.skillify.model.dto.response.SalesPlanAdminPaymentResponseDTO;
import com.temm.skillify.model.entity.SalesPlanAdminPayment;
import com.temm.skillify.model.entity.SalesPlanAdminMembershipEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SalesPlanAdminPaymentMapper {

    private final SalesPlanAdminMembershipEventMapper membershipEventMapper;

    public SalesPlanAdminPaymentResponseDTO toResponseDTO(SalesPlanAdminPayment payment) {
        if (payment == null) {
            return null;
        }

        SalesPlanAdminPaymentResponseDTO dto = new SalesPlanAdminPaymentResponseDTO();
        dto.setId(payment.getId());
        dto.setStartDate(payment.getStartDate());
        dto.setEndDate(payment.getEndDate());
        dto.setExpiresAt(payment.getExpiresAt());
        dto.setConfirmed(payment.isConfirmed());
        dto.setExpired(payment.isExpired());
        dto.setValue(payment.getValue());
        dto.setSalesPlanAdminMembershipEvent(
                membershipEventMapper.toDTO(payment.getSalesPlanAdminMembershipEvent())
        );
        dto.setCreatedAt(payment.getCreatedAt());
        dto.setUpdatedAt(payment.getUpdatedAt());

        return dto;
    }

    public SalesPlanAdminPayment toEntity(SalesPlanAdminPaymentCreateDTO dto) {
        if (dto == null) {
            return null;
        }

        SalesPlanAdminPayment payment = new SalesPlanAdminPayment();
        payment.setStartDate(dto.getStartDate());
        payment.setEndDate(dto.getEndDate());
        payment.setExpiresAt(dto.getExpiresAt());
        payment.setConfirmed(dto.isConfirmed());
        payment.setExpired(dto.isExpired());
        payment.setValue(dto.getValue());

        if (dto.getSalesPlanAdminMembershipEventId() != null) {
            SalesPlanAdminMembershipEvent event = new SalesPlanAdminMembershipEvent();
            event.setId(dto.getSalesPlanAdminMembershipEventId());
            payment.setSalesPlanAdminMembershipEvent(event);
        }

        return payment;
    }

    public void updateEntityFromDTO(SalesPlanAdminPayment payment, SalesPlanAdminPaymentCreateDTO dto) {
        if (payment == null || dto == null) {
            return;
        }

        payment.setStartDate(dto.getStartDate());
        payment.setEndDate(dto.getEndDate());
        payment.setExpiresAt(dto.getExpiresAt());
        payment.setConfirmed(dto.isConfirmed());
        payment.setExpired(dto.isExpired());
        payment.setValue(dto.getValue());

        if (dto.getSalesPlanAdminMembershipEventId() != null) {
            SalesPlanAdminMembershipEvent event = new SalesPlanAdminMembershipEvent();
            event.setId(dto.getSalesPlanAdminMembershipEventId());
            payment.setSalesPlanAdminMembershipEvent(event);
        }
    }

    public List<SalesPlanAdminPaymentResponseDTO> toResponseDTOList(List<SalesPlanAdminPayment> payments) {
        if (payments == null) {
            return null;
        }
        return payments.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}