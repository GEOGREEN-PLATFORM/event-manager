package com.example.event_manager.service.impl;

import com.example.event_manager.controller.EventManagerController;
import com.example.event_manager.entity.*;
import com.example.event_manager.exception.custom.*;
import com.example.event_manager.feignClient.FeignClientUserService;
import com.example.event_manager.model.CreateEventDTO;
import com.example.event_manager.model.CreateHistoryDTO;
import com.example.event_manager.model.UpdateEventDTO;
import com.example.event_manager.model.UserDTO;
import com.example.event_manager.producer.KafkaProducerService;
import com.example.event_manager.producer.dto.UpdateElementDTO;
import com.example.event_manager.repository.*;
import com.example.event_manager.service.EventManagerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.example.event_manager.entity.spec.EntitySpecifications;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
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

    @Autowired
    private final FeignClientUserService feignClientUserService;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    private static final Logger logger = LoggerFactory.getLogger(EventManagerController.class);

    @Override
    @Transactional
    public EventEntity createNewEvent(CreateEventDTO createEventDTO, String token) {
        return eventRepository.save(eventDtoToEntity(createEventDTO, token));
    }

    @Override
    @Transactional
    public EventHistoryEntity createNewHistory(CreateHistoryDTO createHistoryDTO, UUID eventId, String token) {
        getEventById(eventId);
        logger.info("Мероприятие с айди {} найдено в базе данных", eventId);
        return eventHistoryRepository.save(eventHistoryToEntity(createHistoryDTO, eventId, token));
    }

    @Override
    public EventEntity getEventById(UUID eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new EventNotFoundException(eventId)
        );
    }

    @Override
    public Page<EventEntity> getAllEvents(int page, int size,
                                          String status, UUID operatorId,
                                          Instant startFirstDate, Instant startSecondDate,
                                          Instant endFirstDate, Instant endSecondDate,
                                          Instant updateFirstDate, Instant updateSecondDate,
                                          String search, String operatorSearch,
                                          String eventType, String problemAreaType) {
        Pageable pageable = PageRequest.of(page, size);
        Specification<EventEntity> spec = Specification.where(EntitySpecifications.hasStatusValue(status))
                .and(EntitySpecifications.hasOperatorIdValue(operatorId))
                .and(EntitySpecifications.hasStartDateBetween(startFirstDate, startSecondDate))
                .and(EntitySpecifications.hasEndDateBetween(endFirstDate, endSecondDate))
                .and(EntitySpecifications.hasUpdateDateBetween(updateFirstDate, updateSecondDate))
                .and(EntitySpecifications.nameContains(search))
                .and(EntitySpecifications.operaotrNameContains(operatorSearch))
                .and(EntitySpecifications.hasEventType(eventType))
                .and(EntitySpecifications.hasProblemAreaType(problemAreaType));
        return eventRepository.findAll(spec, pageable);
    }

    @Override
    public Page<EventHistoryEntity> getEventHistory(UUID eventId, int page, int size) {
        getEventById(eventId);
        logger.info("Мероприятие с айди {} найдено в базе данных", eventId);
        return eventHistoryRepository.findByEventId(eventId, PageRequest.of(page, size));
    }

    @Deprecated
    @Override
    public List<EventHistoryEntity> getEventHistory(UUID eventId) {
        return getEventHistory(eventId, 0, Integer.MAX_VALUE).getContent();
    }

    @Override
    @Transactional
    public EventEntity updateEvent(UpdateEventDTO updateEventDTO, UUID eventId, String token) {
        EventEntity eventEntity = getEventById(eventId);

        eventEntity.setLastUpdateDate(Instant.now());
        eventEntity.setDescription(updateEventDTO.getDescription() != null ? updateEventDTO.getDescription() : eventEntity.getDescription());
        eventEntity.setName(updateEventDTO.getName() != null ? updateEventDTO.getName() : eventEntity.getName());

        UpdateElementDTO updateElementDTO = new UpdateElementDTO();

        if (updateEventDTO.getEndDate() != null) {

            if (eventEntity.getEndDate() != updateEventDTO.getEndDate()) {
                updateElementDTO.setDate(OffsetDateTime.from(updateEventDTO.getEndDate().atOffset(ZoneOffset.UTC)));
                updateElementDTO.setElementId(eventId);
                updateElementDTO.setType("USER_MARKER");
            }

            eventEntity.setEndDate(updateEventDTO.getEndDate());
        }

        if (updateEventDTO.getStatusCode() != null) {
            StatusEntity statusEntity = statusRepository.findByCode(updateEventDTO.getStatusCode());
            if (statusEntity != null) {

                if (!Objects.equals(eventEntity.getStatusCode(), statusEntity.getCode())) {
                    updateElementDTO.setStatus(statusEntity.getCode());
                    updateElementDTO.setElementId(eventId);
                    updateElementDTO.setType("USER_MARKER");
                }

                eventEntity.setStatusCode(statusEntity.getCode());
            }
            else {
                throw new StatusNotFoundException(updateEventDTO.getStatusCode());
            }
        }

        eventEntity.setOperator(updateEventDTO.getOperatorId() != null ? getUserById(updateEventDTO.getOperatorId(), token) : eventEntity.getOperator());

        logger.info("Мероприятие с айди {} обновлено в базе данных", eventId);

        if (updateElementDTO.getType() != null) {
            kafkaProducerService.sendUpdate(updateElementDTO);
        }
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

    private EventEntity eventDtoToEntity(CreateEventDTO createEventDTO, String token) {
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

        eventEntity.setAuthor(getUserById(createEventDTO.getAuthorId(), token));
        eventEntity.setOperator(getUserById(createEventDTO.getOperatorId(), token));

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

    private EventHistoryEntity eventHistoryToEntity(CreateHistoryDTO createHistoryDTO, UUID eventId, String token) {
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

        eventHistoryEntity.setOperator(getUserById(createHistoryDTO.getOperatorId(), token));

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

    private UserDTO getUserById(UUID userId, String token) {
        if (userId == null) {
            return null;
        }
        return feignClientUserService.getUserById(token, userId);
    }
}
