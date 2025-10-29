package com.temm.skillify.service.student;

import com.temm.skillify.model.dto.response.SalesPlanAdminReturnDTO;
import com.temm.skillify.model.entity.SalesPlanAdmin;
import com.temm.skillify.model.entity.SalesPlanAdminMembershipEvent;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.enums.PlanType;
import com.temm.skillify.repository.SalesPlanAdminRepository;
import com.temm.skillify.repository.UserRepository;
import com.temm.skillify.repository.SalesPlanAdminMembershipEventRepository;
import com.temm.skillify.model.mapper.SalesPlanAdminMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalesPlanAdminPaymentService {
    
}
