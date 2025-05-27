package com.example.event_manager.service.impl;

import com.example.event_manager.entity.*;
import com.example.event_manager.exception.custom.EventNotFoundException;
import com.example.event_manager.exception.custom.HistoryNotFoundException;
import com.example.event_manager.exception.custom.ProblemNotFoundException;
import com.example.event_manager.feignClient.FeignClientGeoMarkerService;
import com.example.event_manager.feignClient.FeignClientUserService;
import com.example.event_manager.model.CreateEventDTO;
import com.example.event_manager.model.CreateHistoryDTO;
import com.example.event_manager.model.UpdateEventDTO;
import com.example.event_manager.model.UserDTO;
import com.example.event_manager.model.geoMarker.GeoMarkerDTO;
import com.example.event_manager.producer.KafkaProducerService;
import com.example.event_manager.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class EventManagerServiceImplTest {

    private EventRepository eventRepository;
    private EventHistoryRepository eventHistoryRepository;
    private StatusRepository statusRepository;
    private ProblemTypeRepository problemTypeRepository;
    private EventTypeRepository eventTypeRepository;
    private FeignClientUserService feignClientUserService;
    private FeignClientGeoMarkerService feignClientGeoMarkerService;
    private KafkaProducerService kafkaProducerService;

    private EventManagerServiceImpl service;

    @BeforeEach
    void setUp() {
        eventRepository = mock(EventRepository.class);
        eventHistoryRepository = mock(EventHistoryRepository.class);
        statusRepository = mock(StatusRepository.class);
        problemTypeRepository = mock(ProblemTypeRepository.class);
        eventTypeRepository = mock(EventTypeRepository.class);
        feignClientUserService = mock(FeignClientUserService.class);
        feignClientGeoMarkerService = mock(FeignClientGeoMarkerService.class);
        kafkaProducerService = mock(KafkaProducerService.class);

        service = new EventManagerServiceImpl(eventRepository, eventHistoryRepository, statusRepository,
                problemTypeRepository, eventTypeRepository, feignClientUserService, feignClientGeoMarkerService, kafkaProducerService);
    }

    @Test
    void createNewEvent_shouldSaveAndPostRelatedTask() {
        CreateEventDTO dto = new CreateEventDTO();
        dto.setGeoPointId(UUID.randomUUID());
        dto.setName("Event Name");
        dto.setProblemAreaType("BORSH");
        dto.setEventType("MONITORING");
        dto.setAuthorId(UUID.randomUUID());
        dto.setOperatorId(UUID.randomUUID());

        EventEntity savedEntity = new EventEntity();
        savedEntity.setId(UUID.randomUUID());
        savedEntity.setGeoPointId(dto.getGeoPointId());

        ProblemTypeEntity problem = new ProblemTypeEntity();
        problem.setCode("BORSH");

        StatusEntity status = new StatusEntity();
        status.setCode("NEW");

        EventTypeEntity eventEntity = new EventTypeEntity();
        eventEntity.setCode("MONITORING");

        when(statusRepository.findDefaultStatus()).thenReturn(status);
        when(problemTypeRepository.findByCode("BORSH")).thenReturn(problem);
        when(eventTypeRepository.findByCode("MONITORING")).thenReturn(eventEntity);
        when(feignClientUserService.getUserById(any(), any())).thenReturn(new UserDTO());
        when(eventRepository.save(any())).thenReturn(savedEntity);

        EventEntity result = service.createNewEvent(dto, "token");

        assertThat(result).isNotNull();
        verify(eventRepository).save(any());
        verify(feignClientGeoMarkerService).postRelatedTask(eq("token"), eq(dto.getGeoPointId()),
                argThat(task -> task.getRelatedTaskId().equals(savedEntity.getId())));
    }

    @Test
    void createNewEvent_shouldThrow_whenProblemTypeNotFound() {
        CreateEventDTO dto = new CreateEventDTO();
        dto.setProblemAreaType("INVALID");
        dto.setEventType("MONITORING");
        dto.setAuthorId(UUID.randomUUID());
        dto.setOperatorId(UUID.randomUUID());
        dto.setGeoPointId(UUID.randomUUID());

        StatusEntity status = new StatusEntity();
        status.setCode("NEW");

        EventTypeEntity eventEntity = new EventTypeEntity();
        eventEntity.setCode("MONITORING");

        when(problemTypeRepository.findByCode("INVALID")).thenReturn(null);
        when(eventTypeRepository.findByCode("MONITORING")).thenReturn(eventEntity);
        when(statusRepository.findDefaultStatus()).thenReturn(status);

        assertThatThrownBy(() -> service.createNewEvent(dto, "token"))
                .isInstanceOf(ProblemNotFoundException.class);
    }

    @Test
    void getAllEvents_shouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "startDate"));
        EventEntity entity = new EventEntity();
        entity.setId(UUID.randomUUID());

        Page<EventEntity> page = new PageImpl<>(List.of(entity));

        when(eventRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);


        Page<EventEntity> result = service.getAllEvents(0, 10, null, null,
                null, null, null, null, null, null,
                null, null, null, null, null, "startDate", Sort.Direction.ASC);

        assertThat(result).hasSize(1);
        verify(eventRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void createNewHistory_shouldSaveHistory() {
        UUID eventId = UUID.randomUUID();
        CreateHistoryDTO dto = new CreateHistoryDTO();
        dto.setRecordType("MONITORING");
        dto.setDescription("desc");
        dto.setOperatorId(UUID.randomUUID());

        EventHistoryEntity saved = new EventHistoryEntity();
        saved.setId(UUID.randomUUID());

        EventTypeEntity eventEntity = new EventTypeEntity();
        eventEntity.setCode("MONITORING");

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(new EventEntity()));
        when(eventTypeRepository.findByCode("MONITORING")).thenReturn(eventEntity);
        when(feignClientUserService.getUserById(any(), any())).thenReturn(new UserDTO());
        when(eventHistoryRepository.save(any())).thenReturn(saved);

        EventHistoryEntity result = service.createNewHistory(dto, eventId, "token");

        assertThat(result).isNotNull();
        verify(eventHistoryRepository).save(any());
    }

    @Test
    void getEventById_shouldReturnEntity() {
        UUID eventId = UUID.randomUUID();
        EventEntity entity = new EventEntity();
        entity.setId(eventId);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(entity));

        EventEntity result = service.getEventById(eventId);

        assertThat(result.getId()).isEqualTo(eventId);
    }

    @Test
    void getEventById_shouldThrow_whenNotFound() {
        UUID id = UUID.randomUUID();
        when(eventRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getEventById(id))
                .isInstanceOf(EventNotFoundException.class);
    }

    @Test
    void getEventHistory_byPage_shouldReturnPage() {
        UUID id = UUID.randomUUID();
        Page<EventHistoryEntity> page = new PageImpl<>(List.of(new EventHistoryEntity()));

        when(eventRepository.findById(id)).thenReturn(Optional.of(new EventEntity()));
        when(eventHistoryRepository.findByEventId(eq(id), any())).thenReturn(page);

        Page<EventHistoryEntity> result = service.getEventHistory(id, 0, 10);

        assertThat(result).hasSize(1);
    }

    @Test
    void getEventHistory_byList_shouldReturnList() {
        UUID id = UUID.randomUUID();
        when(eventRepository.findById(id)).thenReturn(Optional.of(new EventEntity()));
        when(eventHistoryRepository.findByEventId(eq(id), any())).thenReturn(new PageImpl<>(List.of(new EventHistoryEntity())));

        List<EventHistoryEntity> result = service.getEventHistory(id);

        assertThat(result).hasSize(1);
    }

    @Test
    void updateEvent_shouldUpdateFieldsAndSendKafka() {
        UUID id = UUID.randomUUID();
        UpdateEventDTO dto = new UpdateEventDTO();
        dto.setName("Updated Name");
        dto.setStatusCode("UPDATED");
        dto.setEndDate(Instant.now());
        dto.setOperatorId(UUID.randomUUID());

        EventEntity event = new EventEntity();
        event.setId(id);
        event.setStatusCode("OLD");
        event.setOperator(new UserDTO());

        StatusEntity status = new StatusEntity();
        status.setCode("UPDATED");

        when(eventRepository.findById(id)).thenReturn(Optional.of(event));
        when(statusRepository.findByCode("UPDATED")).thenReturn(status);
        when(feignClientUserService.getUserById(any(), any())).thenReturn(new UserDTO());
        when(eventRepository.save(any())).thenReturn(event);

        EventEntity result = service.updateEvent(dto, id, "token");

        assertThat(result.getStatusCode()).isEqualTo("UPDATED");
        verify(kafkaProducerService).sendUpdate(any());
    }

    @Test
    void deleteEvent_shouldRemoveHistoryAndEvent() {
        UUID eventId = UUID.randomUUID();
        UUID historyId = UUID.randomUUID();

        EventHistoryEntity history = new EventHistoryEntity();
        history.setId(historyId);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(new EventEntity()));
        when(eventHistoryRepository.findByEventId(eq(eventId), any())).thenReturn(new PageImpl<>(List.of(history)));
        when(eventHistoryRepository.findById(historyId)).thenReturn(Optional.of(history));

        service.deleteEvent(eventId);

        verify(eventRepository).delete(any(EventEntity.class));
        verify(eventHistoryRepository).delete(any(EventHistoryEntity.class));

    }

    @Test
    void getEventHistoryById_shouldReturnHistory() {
        UUID id = UUID.randomUUID();
        EventHistoryEntity history = new EventHistoryEntity();
        history.setId(id);

        when(eventHistoryRepository.findById(id)).thenReturn(Optional.of(history));

        EventHistoryEntity result = service.getEventHistoryById(id);

        assertThat(result.getId()).isEqualTo(id);
    }

    @Test
    void getEventHistoryById_shouldThrow_whenNotFound() {
        UUID id = UUID.randomUUID();
        when(eventHistoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getEventHistoryById(id))
                .isInstanceOf(HistoryNotFoundException.class);
    }





}
