package com.temm.skillify.repository;

import com.temm.skillify.model.entity.SalesPlanAdmin;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.enums.PlanType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface SalesPlanAdminRepository extends JpaRepository<SalesPlanAdmin, String> {

    List<SalesPlanAdmin> findByName(String name);

    List<SalesPlanAdmin> findByDescription(String description);

    List<SalesPlanAdmin> findByPrice(BigDecimal price);

    List<SalesPlanAdmin> findByType(PlanType type);

    List<SalesPlanAdmin> findByResourcesContaining(String resource);

    List<SalesPlanAdmin> findByCreator(User creator);
}