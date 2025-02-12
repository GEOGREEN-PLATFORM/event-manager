package com.example.event_manager.repository;

import com.example.event_manager.entity.EventHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventHistoryRepository extends JpaRepository<EventHistoryEntity, UUID> {

    List<EventHistoryEntity> findByEventId(UUID eventId);
}
