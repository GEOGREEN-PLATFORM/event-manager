package com.example.event_manager.service.impl;

import com.example.event_manager.entity.EventTypeEntity;
import com.example.event_manager.model.ResponseDTO;
import com.example.event_manager.model.eventType.EventTypeDTO;
import com.example.event_manager.repository.EventHistoryRepository;
import com.example.event_manager.repository.EventRepository;
import com.example.event_manager.repository.EventTypeRepository;
import com.example.event_manager.service.EventTypeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventTypeServiceImpl implements EventTypeService {

    @Autowired
    private final EventTypeRepository eventTypeRepository;

    @Autowired
    private final EventRepository eventRepository;

    @Autowired
    private final EventHistoryRepository eventHistoryRepository;

    @Override
    @Transactional
    public EventTypeEntity saveNewEventType(EventTypeDTO eventTypeDTO) {

        EventTypeEntity eventTypeEntity = new EventTypeEntity();
        eventTypeEntity.setCode(eventTypeDTO.getCode());
        eventTypeEntity.setDescription(eventTypeDTO.getDescription());

        return eventTypeRepository.save(eventTypeEntity);
    }

    @Override
    public List<EventTypeEntity> getAllEventTypes() {
        return eventTypeRepository.findAll();
    }

    @Override
    public Optional<EventTypeEntity> getEventTypeById(Integer id) {
        return eventTypeRepository.findById(id);
    }

    @Override
    @Transactional
    public ResponseDTO deleteEventTypeById(Integer id) {
        Optional<EventTypeEntity> eventTypeEntity = eventTypeRepository.findById(id);
        ResponseDTO response;

        if (eventTypeEntity.isPresent()) {
            if (eventTypeEntity.get().isDefault()) {
                response = new ResponseDTO(HttpStatus.NOT_FOUND, "Нельзя удалить дефолтный тип мероприятия");
            }
            else {
                EventTypeEntity defaultProblem = eventTypeRepository.findDefaultEventType();
                int updateCount = eventRepository.updateEventTypeForMarkers(eventTypeEntity.get().getCode(), defaultProblem.getCode());
                updateCount = eventHistoryRepository.updateEventTypeForMarkers(eventTypeEntity.get().getCode(), defaultProblem.getCode());

                eventTypeRepository.deleteById(id);
                response = new ResponseDTO(HttpStatus.OK, "Тип мероприятия удален!");
            }
        }
        else {
            response = new ResponseDTO(HttpStatus.NOT_FOUND, "Тип мероприятия c айди " + id + " не найден!");
        }

        return response;
    }
}
