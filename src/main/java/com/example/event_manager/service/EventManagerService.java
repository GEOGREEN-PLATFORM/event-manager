package com.example.event_manager.service;

import com.example.event_manager.entity.EventEntity;
import com.example.event_manager.model.CreateEventDTO;

public interface EventManagerService {
    EventEntity createNewEvent(CreateEventDTO createEventDTO);
}
