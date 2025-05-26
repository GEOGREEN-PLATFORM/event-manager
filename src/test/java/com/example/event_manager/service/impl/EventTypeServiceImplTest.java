package com.example.event_manager.service.impl;

import com.example.event_manager.entity.EventTypeEntity;
import com.example.event_manager.model.ResponseDTO;
import com.example.event_manager.model.eventType.EventTypeDTO;
import com.example.event_manager.repository.EventHistoryRepository;
import com.example.event_manager.repository.EventRepository;
import com.example.event_manager.repository.EventTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class EventTypeServiceImplTest {

    private EventTypeRepository eventTypeRepository;
    private EventRepository eventRepository;
    private EventHistoryRepository eventHistoryRepository;

    private EventTypeServiceImpl service;

    @BeforeEach
    void setUp() {
        eventTypeRepository = mock(EventTypeRepository.class);
        eventRepository = mock(EventRepository.class);
        eventHistoryRepository = mock(EventHistoryRepository.class);

        service = new EventTypeServiceImpl(eventTypeRepository, eventRepository, eventHistoryRepository);
    }

    @Test
    @DisplayName("saveNewEventType должен сохранить и вернуть сущность")
    void saveNewEventType_shouldSave() {
        EventTypeDTO dto = new EventTypeDTO();
        dto.setCode("TYPE1");
        dto.setDescription("Description");

        EventTypeEntity savedEntity = new EventTypeEntity();
        savedEntity.setCode("TYPE1");
        savedEntity.setDescription("Description");

        when(eventTypeRepository.save(any())).thenReturn(savedEntity);

        var result = service.saveNewEventType(dto);

        assertThat(result.getCode()).isEqualTo("TYPE1");
        verify(eventTypeRepository).save(any(EventTypeEntity.class));
    }

    @Test
    @DisplayName("getAllEventTypes должен вернуть все типы мероприятий")
    void getAllEventTypes_shouldReturnAll() {
        when(eventTypeRepository.findAll()).thenReturn(List.of(new EventTypeEntity(), new EventTypeEntity()));

        var result = service.getAllEventTypes();

        assertThat(result).hasSize(2);
        verify(eventTypeRepository).findAll();
    }

    @Test
    @DisplayName("getEventTypeById должен вернуть Optional сущности")
    void getEventTypeById_shouldReturnOptional() {
        EventTypeEntity entity = new EventTypeEntity();
        entity.setId(1);
        when(eventTypeRepository.findById(1)).thenReturn(Optional.of(entity));

        var result = service.getEventTypeById(1);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1);
    }

    @Test
    @DisplayName("deleteEventTypeById должен вернуть 404 если не найден")
    void deleteEventTypeById_shouldReturnNotFound() {
        when(eventTypeRepository.findById(99)).thenReturn(Optional.empty());

        ResponseDTO response = service.deleteEventTypeById(99);

        assertThat(response.getStatus()).hasToString("404 NOT_FOUND");
        assertThat(response.getMessage()).contains("не найден");
    }

    @Test
    @DisplayName("deleteEventTypeById должен вернуть 404 если дефолтный")
    void deleteEventTypeById_shouldReturnNotFoundOnDefault() {
        EventTypeEntity entity = new EventTypeEntity();
        entity.setCode("DEFAULT");
        entity.setDefault(true);

        when(eventTypeRepository.findById(1)).thenReturn(Optional.of(entity));

        ResponseDTO response = service.deleteEventTypeById(1);

        assertThat(response.getStatus()).hasToString("404 NOT_FOUND");
        assertThat(response.getMessage()).contains("Нельзя удалить дефолтный");
    }

    @Test
    @DisplayName("deleteEventTypeById должен обновить и удалить")
    void deleteEventTypeById_shouldUpdateAndDelete() {
        EventTypeEntity entity = new EventTypeEntity();
        entity.setCode("TO_DELETE");
        entity.setDefault(false);

        EventTypeEntity defaultEntity = new EventTypeEntity();
        defaultEntity.setCode("DEFAULT");

        when(eventTypeRepository.findById(1)).thenReturn(Optional.of(entity));
        when(eventTypeRepository.findDefaultEventType()).thenReturn(defaultEntity);
        when(eventRepository.updateEventTypeForMarkers("TO_DELETE", "DEFAULT")).thenReturn(1);
        when(eventHistoryRepository.updateEventTypeForMarkers("TO_DELETE", "DEFAULT")).thenReturn(1);

        ResponseDTO response = service.deleteEventTypeById(1);

        assertThat(response.getStatus()).hasToString("200 OK");
        assertThat(response.getMessage()).contains("удален");
        verify(eventTypeRepository).deleteById(1);
    }
}
