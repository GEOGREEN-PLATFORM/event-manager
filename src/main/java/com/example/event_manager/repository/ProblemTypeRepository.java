package com.example.event_manager.repository;

import com.example.event_manager.entity.ProblemTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemTypeRepository extends JpaRepository<ProblemTypeEntity, Integer> {

    @Query("SELECT s FROM ProblemTypeEntity s WHERE s.isDefault = true")
    ProblemTypeEntity findDefaultProblem();

    ProblemTypeEntity findByCode(String code);
}