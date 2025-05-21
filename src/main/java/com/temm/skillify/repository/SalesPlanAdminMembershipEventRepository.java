package com.temm.skillify.repository;

import com.temm.skillify.model.entity.SalesPlan;
import com.temm.skillify.model.entity.SalesPlanAdmin;
import com.temm.skillify.model.entity.SalesPlanAdminMembershipEvent;
import com.temm.skillify.model.entity.User;
import com.temm.skillify.model.enums.SalesPlanMembershipType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SalesPlanAdminMembershipEventRepository extends JpaRepository<SalesPlanAdminMembershipEvent, String> {

    List<SalesPlanAdminMembershipEvent> findByCustomer(User customer);

    List<SalesPlanAdminMembershipEvent> findByStatus(SalesPlanMembershipType status);
}