package com.example.event_manager.repository;

import com.example.event_manager.entity.EventEntity;
import com.example.event_manager.entity.StatusEntity;
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
    int updateStatusForEvents(StatusEntity oldStatus, StatusEntity newStatus);

}
