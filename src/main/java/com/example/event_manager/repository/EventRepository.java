package com.example.event_manager.repository;

import com.example.event_manager.entity.EventEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, UUID> {

    @Transactional
    @Modifying
    @Query("UPDATE EventEntity u SET u.statusCode = :newStatus WHERE u.statusCode = :oldStatus")
    int updateStatusForEvents(String oldStatus, String newStatus);

    @Transactional
    @Modifying
    @Query("UPDATE EventEntity u SET u.problemAreaType = :newProblem WHERE u.problemAreaType = :oldProblem")
    int updateProblemForMarkers(String oldProblem, String newProblem);

    @Transactional
    @Modifying
    @Query("UPDATE EventEntity u SET u.eventType = :newProblem WHERE u.eventType = :oldProblem")
    int updateEventTypeForMarkers(String oldProblem, String newProblem);

    @Override
    @NotNull
    Page<EventEntity> findAll(@NotNull Pageable pageable);

}
