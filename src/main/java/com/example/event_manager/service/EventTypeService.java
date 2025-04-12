package com.example.event_manager.service;

import com.example.event_manager.entity.EventTypeEntity;
import com.example.event_manager.model.ResponseDTO;
import com.example.event_manager.model.eventType.EventTypeDTO;

import java.util.List;
import java.util.Optional;

public interface EventTypeService {

    EventTypeEntity saveNewEventType(EventTypeDTO eventTypeDTO);

    List<EventTypeEntity> getAllEventTypes();

    Optional<EventTypeEntity> getEventTypeById(Integer id);

    ResponseDTO deleteEventTypeById(Integer id);
}
