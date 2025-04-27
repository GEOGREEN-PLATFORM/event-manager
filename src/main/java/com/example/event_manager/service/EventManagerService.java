package com.example.event_manager.service;

import com.example.event_manager.entity.EventEntity;
import com.example.event_manager.entity.EventHistoryEntity;
import com.example.event_manager.model.CreateEventDTO;
import com.example.event_manager.model.CreateHistoryDTO;
import com.example.event_manager.model.UpdateEventDTO;
import org.springframework.data.domain.Page;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface EventManagerService {
    EventEntity createNewEvent(CreateEventDTO createEventDTO);

    EventHistoryEntity createNewHistory(CreateHistoryDTO createHistoryDTO, UUID eventId);

    EventEntity getEventById(UUID eventId);

    Page<EventEntity> getAllEvents(int page, int size, String status, String operatorName,
                                   Instant startFirstDate, Instant startSecondDate,
                                   Instant endFirstDate, Instant endSecondDate,
                                   Instant updateFirstDate, Instant updateSecondDate, String search);

    Page<EventHistoryEntity> getEventHistory(UUID eventId, int page, int size);

    List<EventHistoryEntity> getEventHistory(UUID eventId);

    EventEntity updateEvent(UpdateEventDTO updateEventDTO, UUID eventId);

    EventHistoryEntity getEventHistoryById(UUID historyId);

    void deleteHistory(UUID eventId, UUID historyId);

    void deleteEvent(UUID eventId);
}
