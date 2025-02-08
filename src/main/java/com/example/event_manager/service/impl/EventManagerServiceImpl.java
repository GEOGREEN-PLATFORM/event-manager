package com.example.event_manager.service.impl;

import com.example.event_manager.entity.EventEntity;
import com.example.event_manager.model.CreateEventDTO;
import com.example.event_manager.repository.EventRepository;
import com.example.event_manager.service.EventManagerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class EventManagerServiceImpl implements EventManagerService {

    @Autowired
    private EventRepository eventRepository;

    @Override
    @Transactional
    public EventEntity createNewEvent(CreateEventDTO createEventDTO) {
        return eventRepository.save(eventDtoToEntity(createEventDTO));
    }

    private EventEntity eventDtoToEntity(CreateEventDTO createEventDTO) {
        EventEntity eventEntity = new EventEntity();

        eventEntity.setSourceId(createEventDTO.getSourceId());

        if (createEventDTO.getStartDate() == null) {
            eventEntity.setStartDate(LocalDate.now());
        }
        else {
            eventEntity.setStartDate(createEventDTO.getStartDate());
        }

        eventEntity.setLastUpdateDate(LocalDate.now());
        eventEntity.setStatus("ПЛАНИРУЕТСЯ");
        eventEntity.setEventType(createEventDTO.getEventType());
        eventEntity.setDescription(createEventDTO.getDescription());

        return eventEntity;
    }
}
