package com.example.event_manager.service;

import com.example.event_manager.entity.EventEntity;
import com.example.event_manager.entity.EventHistoryEntity;
import com.example.event_manager.model.CreateEventDTO;
import com.example.event_manager.model.CreateHistoryDTO;
import com.example.event_manager.model.UpdateEventDTO;

import java.util.List;
import java.util.UUID;

public interface EventManagerService {
    EventEntity createNewEvent(CreateEventDTO createEventDTO);

    EventHistoryEntity createNewHistory(CreateHistoryDTO createHistoryDTO, UUID eventId);

    EventEntity getEventById(UUID eventId);
    List<EventEntity> getAllEvents();

    List<EventHistoryEntity> getEventHistory(UUID eventId);

    EventEntity updateEvent(UpdateEventDTO updateEventDTO, UUID eventId);

    EventHistoryEntity getEventHistoryById(UUID historyId);

    void deleteHistory(UUID eventId, UUID historyId);

    void deleteEvent(UUID eventId);
}
