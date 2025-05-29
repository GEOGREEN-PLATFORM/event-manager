package com.example.event_manager.service.impl;

import com.example.event_manager.entity.StatusEntity;
import com.example.event_manager.model.status.StatusDTO;
import com.example.event_manager.repository.EventRepository;
import com.example.event_manager.repository.StatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class StatusServiceImplTest {

    private StatusRepository statusRepository;
    private EventRepository eventRepository;
    private StatusServiceImpl service;

    @BeforeEach
    void setUp() {
        statusRepository = mock(StatusRepository.class);
        eventRepository = mock(EventRepository.class);
        service = new StatusServiceImpl(statusRepository, eventRepository);
    }

    @Test
    @DisplayName("saveNewStatus должен сохранить и вернуть сущность")
    void saveNewStatus_shouldSave() {
        StatusDTO dto = new StatusDTO();
        dto.setCode("NEW");
        dto.setDescription("New status");

        StatusEntity saved = new StatusEntity();
        saved.setCode("NEW");
        saved.setDescription("New status");

        when(statusRepository.save(any())).thenReturn(saved);

        StatusEntity result = service.saveNewStatus(dto);

        assertThat(result.getCode()).isEqualTo("NEW");
        assertThat(result.getDescription()).isEqualTo("New status");
        verify(statusRepository).save(any());
    }

    @Test
    @DisplayName("getAllStatuses должен вернуть список всех статусов")
    void getAllStatuses_shouldReturnAll() {
        when(statusRepository.findAll()).thenReturn(List.of(new StatusEntity(), new StatusEntity()));

        var result = service.getAllStatuses();

        assertThat(result).hasSize(2);
        verify(statusRepository).findAll();
    }

    @Test
    @DisplayName("findStatusById должен вернуть Optional по ID")
    void findStatusById_shouldReturnOptional() {
        StatusEntity status = new StatusEntity();
        status.setId(1);

        when(statusRepository.findById(1)).thenReturn(Optional.of(status));

        var result = service.findStatusById(1);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1);
    }

    @Test
    @DisplayName("deleteStatus должен вернуть NOT_FOUND если статус не найден")
    void deleteStatus_shouldReturnNotFoundIfMissing() {
        when(statusRepository.findById(1)).thenReturn(Optional.empty());

        var result = service.deleteStatus(1);

        assertThat(result.getStatus().value()).isEqualTo(404);
        assertThat(result.getMessage()).contains("не найден");
    }

    @Test
    @DisplayName("deleteStatus должен вернуть NOT_FOUND если статус дефолтный")
    void deleteStatus_shouldReturnNotFoundIfDefault() {
        StatusEntity entity = new StatusEntity();
        entity.setCode("DEF");
        entity.setDefault(true);

        when(statusRepository.findById(1)).thenReturn(Optional.of(entity));

        var result = service.deleteStatus(1);

        assertThat(result.getStatus().value()).isEqualTo(404);
        assertThat(result.getMessage()).contains("дефолтный");
    }

    @Test
    @DisplayName("deleteStatus должен обновить связанные сущности и удалить статус")
    void deleteStatus_shouldUpdateAndDelete() {
        StatusEntity toDelete = new StatusEntity();
        toDelete.setId(1);
        toDelete.setCode("OLD");
        toDelete.setDefault(false);

        StatusEntity defaultStatus = new StatusEntity();
        defaultStatus.setCode("DEFAULT");

        when(statusRepository.findById(1)).thenReturn(Optional.of(toDelete));
        when(statusRepository.findDefaultStatus()).thenReturn(defaultStatus);
        when(eventRepository.updateStatusForEvents("OLD", "DEFAULT")).thenReturn(3);

        var result = service.deleteStatus(1);

        assertThat(result.getStatus().value()).isEqualTo(200);
        assertThat(result.getMessage()).contains("удалён");
        verify(eventRepository).updateStatusForEvents("OLD", "DEFAULT");
        verify(statusRepository).deleteById(1);
    }
}
