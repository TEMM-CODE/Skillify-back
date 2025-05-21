package com.temm.skillify.repository;

import com.temm.skillify.model.entity.SalesPlan;
import com.temm.skillify.model.entity.SalesPlanMembershipEvent;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.enums.SalesPlanMembershipType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SalesPlanMembershipEventRepository extends JpaRepository<SalesPlanMembershipEvent, String> {

    List<SalesPlanMembershipEvent> findByCustomer(User customer);

    List<SalesPlanMembershipEvent> findBySalesPlan(SalesPlan salesPlan);

    List<SalesPlanMembershipEvent> findByStatus(SalesPlanMembershipType status);
}