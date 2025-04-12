package com.example.event_manager.repository;

import com.example.event_manager.entity.EventTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EventTypeRepository extends JpaRepository<EventTypeEntity, Integer> {

    @Query("SELECT s FROM EventTypeEntity s WHERE s.isDefault = true")
    EventTypeEntity findDefaultEventType();

    EventTypeEntity findByCode(String code);
}