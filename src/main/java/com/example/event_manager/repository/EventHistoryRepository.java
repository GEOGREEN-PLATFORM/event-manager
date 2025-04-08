package com.example.event_manager.repository;

import com.example.event_manager.entity.EventHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventHistoryRepository extends JpaRepository<EventHistoryEntity, UUID> {

    List<EventHistoryEntity> findByEventId(UUID eventId);

    @Transactional
    @Modifying
    @Query("UPDATE EventHistoryEntity u SET u.recordType = :newProblem WHERE u.recordType = :oldProblem")
    int updateEventTypeForMarkers(String oldProblem, String newProblem);
}
