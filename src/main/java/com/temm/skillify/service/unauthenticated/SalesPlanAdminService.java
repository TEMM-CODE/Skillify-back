package com.temm.skillify.service.unauthenticated;

import com.temm.skillify.model.dto.request.SalesPlanAdminCreateDTO;
import com.temm.skillify.model.dto.response.SalesPlanAdminReturnDTO;
import com.temm.skillify.model.entity.SalesPlanAdmin;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.mapper.SalesPlanAdminMapper;
import com.temm.skillify.repository.SalesPlanAdminRepository;
import com.temm.skillify.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalesPlanAdminService {

    private final SalesPlanAdminRepository salesPlanAdminRepository;
    private final SalesPlanAdminMapper salesPlanAdminMapper;
    private final UserRepository userRepository;

    public List<SalesPlanAdminReturnDTO> getAll() {
        return salesPlanAdminRepository.findAll()
                .stream()
                .map(salesPlanAdminMapper::toDTO)
                .collect(Collectors.toList());
    }

    public SalesPlanAdminReturnDTO getById(String id) {
        SalesPlanAdmin salesPlanAdmin = salesPlanAdminRepository.findById(id)
                .orElse(null);
        return salesPlanAdminMapper.toDTO(salesPlanAdmin);
    }

    public List<SalesPlanAdminReturnDTO> getByCreator(String creatorId){
        User creator = userRepository.findById(creatorId).orElseThrow();
        return salesPlanAdminRepository.findByCreator(creator)
                .stream()
                .map(salesPlanAdminMapper::toDTO)
                .collect(Collectors.toList());
    }
}