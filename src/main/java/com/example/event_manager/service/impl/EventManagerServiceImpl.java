package com.example.event_manager.service.impl;

import com.example.event_manager.controller.EventManagerController;
import com.example.event_manager.entity.EventEntity;
import com.example.event_manager.entity.EventHistoryEntity;
import com.example.event_manager.entity.StatusEntity;
import com.example.event_manager.exception.custom.EventNotFoundException;
import com.example.event_manager.exception.custom.HistoryNotFoundException;
import com.example.event_manager.exception.custom.StatusNotFoundException;
import com.example.event_manager.model.CreateEventDTO;
import com.example.event_manager.model.CreateHistoryDTO;
import com.example.event_manager.model.UpdateEventDTO;
import com.example.event_manager.repository.EventHistoryRepository;
import com.example.event_manager.repository.EventRepository;
import com.example.event_manager.repository.StatusRepository;
import com.example.event_manager.service.EventManagerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

        eventEntity.setLastUpdateDate(LocalDate.now());
        eventEntity.setDescription(updateEventDTO.getDescription() != null ? updateEventDTO.getDescription() : eventEntity.getDescription());
        eventEntity.setName(updateEventDTO.getName() != null ? updateEventDTO.getName() : eventEntity.getName());

        if (updateEventDTO.getEndDate() != null) {
            eventEntity.setEndDate(updateEventDTO.getEndDate());
        }

        if (updateEventDTO.getStatusCode() != null) {
            StatusEntity statusEntity = statusRepository.findByCode(updateEventDTO.getStatusCode());
            if (statusEntity != null) {
                eventEntity.setStatusCode(statusEntity);
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
            eventEntity.setStartDate(LocalDate.now());
        }
        else {
            eventEntity.setStartDate(createEventDTO.getStartDate());
        }

        if (createEventDTO.getEndDate() != null) {
            eventEntity.setEndDate(createEventDTO.getEndDate());
        }

        eventEntity.setLastUpdateDate(LocalDate.now());
        eventEntity.setStatusCode(statusRepository.findDefaultStatus());
        eventEntity.setEventType(createEventDTO.getEventType());
        eventEntity.setName(createEventDTO.getName());
        eventEntity.setDescription(createEventDTO.getDescription());

        eventEntity.setAuthorId(createEventDTO.getAuthorId());
        eventEntity.setAuthorName("Иванов И.И.");
        // TODO запрашивать имя у Даши

        eventEntity.setOperatorId(createEventDTO.getOperatorId());
        eventEntity.setOperatorName(createEventDTO.getOperatorId() != null ? "Иванов И.И." : null);
        // TODO запрашивать имя у Даши

        return eventEntity;
    }

    private EventHistoryEntity eventHistoryToEntity(CreateHistoryDTO createHistoryDTO, UUID eventId) {
        // TODO Сделать проверку на корректное dto
        EventHistoryEntity eventHistoryEntity = new EventHistoryEntity();

        eventHistoryEntity.setEventId(eventId);

        if (createHistoryDTO.getRecordDate() == null) {
            eventHistoryEntity.setRecordDate(LocalDate.now());
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

        eventHistoryEntity.setCreateDate(LocalDateTime.now());

        return eventHistoryEntity;
    }
}
