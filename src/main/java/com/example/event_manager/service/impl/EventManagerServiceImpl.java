package com.example.event_manager.service.impl;

import com.example.event_manager.controller.EventManagerController;
import com.example.event_manager.entity.*;
import com.example.event_manager.exception.custom.*;
import com.example.event_manager.model.CreateEventDTO;
import com.example.event_manager.model.CreateHistoryDTO;
import com.example.event_manager.model.UpdateEventDTO;
import com.example.event_manager.repository.*;
import com.example.event_manager.service.EventManagerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventManagerServiceImpl implements EventManagerService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventHistoryRepository eventHistoryRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private ProblemTypeRepository problemTypeRepository;

    @Autowired
    private EventTypeRepository eventTypeRepository;

    private static final Logger logger = LoggerFactory.getLogger(EventManagerController.class);

    @Override
    @Transactional
    public EventEntity createNewEvent(CreateEventDTO createEventDTO) {
        return eventRepository.save(eventDtoToEntity(createEventDTO));
    }

    @Override
    @Transactional
    public EventHistoryEntity createNewHistory(CreateHistoryDTO createHistoryDTO, UUID eventId) {
        getEventById(eventId);
        logger.info("Мероприятие с айди {} найдено в базе данных", eventId);
        return eventHistoryRepository.save(eventHistoryToEntity(createHistoryDTO, eventId));
    }

    @Override
    public EventEntity getEventById(UUID eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new EventNotFoundException(eventId)
        );
    }

    @Override
    public List<EventEntity> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public List<EventHistoryEntity> getEventHistory(UUID eventId) {
        getEventById(eventId);
        logger.info("Мероприятие с айди {} найдено в базе данных", eventId);
        return eventHistoryRepository.findByEventId(eventId);
    }

    @Override
    @Transactional
    public EventEntity updateEvent(UpdateEventDTO updateEventDTO, UUID eventId) {
        EventEntity eventEntity = getEventById(eventId);

        eventEntity.setLastUpdateDate(Instant.now());
        eventEntity.setDescription(updateEventDTO.getDescription() != null ? updateEventDTO.getDescription() : eventEntity.getDescription());
        eventEntity.setName(updateEventDTO.getName() != null ? updateEventDTO.getName() : eventEntity.getName());

        if (updateEventDTO.getEndDate() != null) {
            eventEntity.setEndDate(updateEventDTO.getEndDate());
        }

        if (updateEventDTO.getStatusCode() != null) {
            StatusEntity statusEntity = statusRepository.findByCode(updateEventDTO.getStatusCode());
            if (statusEntity != null) {
                eventEntity.setStatusCode(statusEntity.getCode());
            }
            else {
                throw new StatusNotFoundException(updateEventDTO.getStatusCode());
            }
        }

        eventEntity.setOperatorId(updateEventDTO.getOperatorId() != null ? updateEventDTO.getOperatorId() : eventEntity.getOperatorId());
        eventEntity.setOperatorName(updateEventDTO.getOperatorId() != null ? "Иванов И.И." : eventEntity.getOperatorName());
        // TODO запрашивать имя у Даши

        logger.info("Мероприятие с айди {} обновлено в базе данных", eventId);
        return eventRepository.save(eventEntity);
    }

    @Override
    @Transactional
    public void deleteHistory(UUID eventId, UUID historyId) {
        getEventById(eventId);
        eventHistoryRepository.delete(getEventHistoryById(historyId));
        logger.info("История с айди {} удалена", historyId);
    }

    @Override
    @Transactional
    public void deleteEvent(UUID eventId) {
        List<EventHistoryEntity> histories = getEventHistory(eventId);

        for (EventHistoryEntity history : histories) {
            deleteHistory(eventId, history.getId());
        }

        eventRepository.delete(getEventById(eventId));
        logger.info("Мероприятие с айди {} удалено", eventId);
    }

    @Override
    public EventHistoryEntity getEventHistoryById(UUID historyId) {
        return eventHistoryRepository.findById(historyId).orElseThrow(
                () -> new HistoryNotFoundException(historyId)
        );
    }

    private EventEntity eventDtoToEntity(CreateEventDTO createEventDTO) {
        // TODO Сделать проверку на корректное dto
        EventEntity eventEntity = new EventEntity();

        eventEntity.setGeoPointId(createEventDTO.getGeoPointId());

        if (createEventDTO.getStartDate() == null) {
            eventEntity.setStartDate(Instant.now());
        }
        else {
            eventEntity.setStartDate(createEventDTO.getStartDate());
        }

        if (createEventDTO.getEndDate() != null) {
            eventEntity.setEndDate(createEventDTO.getEndDate());
        }

        eventEntity.setLastUpdateDate(Instant.now());
        eventEntity.setStatusCode(statusRepository.findDefaultStatus().getCode());
        eventEntity.setEventType(createEventDTO.getEventType());
        eventEntity.setName(createEventDTO.getName());
        eventEntity.setDescription(createEventDTO.getDescription());

        eventEntity.setAuthorId(createEventDTO.getAuthorId());
        eventEntity.setAuthorName("Иванов И.И.");
        // TODO запрашивать имя у Даши

        eventEntity.setOperatorId(createEventDTO.getOperatorId());
        eventEntity.setOperatorName(createEventDTO.getOperatorId() != null ? "Иванов И.И." : null);
        // TODO запрашивать имя у Даши

        ProblemTypeEntity problemTypeEntity = problemTypeRepository.findByCode(createEventDTO.getProblemAreaType());
        if (problemTypeEntity != null) {
            eventEntity.setProblemAreaType(problemTypeEntity.getCode());
        }
        else {
            throw new ProblemNotFoundException(createEventDTO.getProblemAreaType());
        }


        EventTypeEntity eventTypeEntity = eventTypeRepository.findByCode(createEventDTO.getEventType());
        if (eventTypeEntity != null) {
            eventEntity.setEventType(eventTypeEntity.getCode());
        }
        else {
            throw new EventTypeNotFoundException(createEventDTO.getEventType());
        }

        return eventEntity;
    }

    private EventHistoryEntity eventHistoryToEntity(CreateHistoryDTO createHistoryDTO, UUID eventId) {
        // TODO Сделать проверку на корректное dto
        EventHistoryEntity eventHistoryEntity = new EventHistoryEntity();

        eventHistoryEntity.setEventId(eventId);

        if (createHistoryDTO.getRecordDate() == null) {
            eventHistoryEntity.setRecordDate(Instant.now());
        }
        else {
            eventHistoryEntity.setRecordDate(createHistoryDTO.getRecordDate());
        }

        eventHistoryEntity.setRecordType(createHistoryDTO.getRecordType());
        eventHistoryEntity.setDescription(createHistoryDTO.getDescription() != null ? createHistoryDTO.getDescription() : "");
        eventHistoryEntity.setPhotos(createHistoryDTO.getPhotos() != null ? createHistoryDTO.getPhotos() : List.of());

        eventHistoryEntity.setOperatorId(createHistoryDTO.getOperatorId());
        eventHistoryEntity.setOperatorName("Иванов И.И.");
        // TODO запрашивать имя у Даши

        eventHistoryEntity.setCreateDate(Instant.now());

        EventTypeEntity eventTypeEntity = eventTypeRepository.findByCode(createHistoryDTO.getRecordType());
        if (eventTypeEntity != null) {
            eventHistoryEntity.setRecordType(eventTypeEntity.getCode());
        }
        else {
            throw new EventTypeNotFoundException(createHistoryDTO.getRecordType());
        }

        return eventHistoryEntity;
    }
}
