package com.temm.skillify.repository;

import com.temm.skillify.model.entity.MentorMembership;
import com.temm.skillify.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MentorMembershipRepository extends JpaRepository<MentorMembership, String> {
    List<MentorMembership> findByMentor(User mentor);
    List<MentorMembership> findByAdmin(User admin);
}