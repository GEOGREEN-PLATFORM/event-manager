package com.example.event_manager.service.impl;

import com.example.event_manager.entity.ProblemTypeEntity;
import com.example.event_manager.model.ResponseDTO;
import com.example.event_manager.model.problemType.ProblemTypeDTO;
import com.example.event_manager.repository.EventRepository;
import com.example.event_manager.repository.ProblemTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProblemTypeServiceImplTest {

    private ProblemTypeRepository problemTypeRepository;
    private EventRepository eventRepository;
    private ProblemTypeServiceImpl service;

    @BeforeEach
    void setUp() {
        problemTypeRepository = mock(ProblemTypeRepository.class);
        eventRepository = mock(EventRepository.class);
        service = new ProblemTypeServiceImpl(problemTypeRepository, eventRepository);
    }

    @Test
    @DisplayName("saveNewProblem должен сохранить и вернуть сущность")
    void saveNewProblem_shouldSave() {
        ProblemTypeDTO dto = new ProblemTypeDTO();
        dto.setCode("CODE1");
        dto.setDescription("desc");

        ProblemTypeEntity savedEntity = new ProblemTypeEntity();
        savedEntity.setCode("CODE1");
        savedEntity.setDescription("desc");

        when(problemTypeRepository.save(any())).thenReturn(savedEntity);

        ProblemTypeEntity result = service.saveNewProblem(dto);

        assertThat(result.getCode()).isEqualTo("CODE1");
        assertThat(result.getDescription()).isEqualTo("desc");
        verify(problemTypeRepository).save(any());
    }

    @Test
    @DisplayName("getAllProblems должен вернуть все сущности")
    void getAllProblems_shouldReturnList() {
        when(problemTypeRepository.findAll()).thenReturn(List.of(new ProblemTypeEntity(), new ProblemTypeEntity()));

        var result = service.getAllProblems();

        assertThat(result).hasSize(2);
        verify(problemTypeRepository).findAll();
    }

    @Test
    @DisplayName("getProblemById должен вернуть Optional")
    void getProblemById_shouldReturnOptional() {
        ProblemTypeEntity entity = new ProblemTypeEntity();
        entity.setId(1);
        when(problemTypeRepository.findById(1)).thenReturn(Optional.of(entity));

        var result = service.getProblemById(1);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1);
    }

    @Test
    @DisplayName("deleteProblemById должен вернуть 404 если не найден")
    void deleteProblemById_shouldReturnNotFoundIfMissing() {
        when(problemTypeRepository.findById(10)).thenReturn(Optional.empty());

        ResponseDTO response = service.deleteProblemById(10);

        assertThat(response.getStatus().value()).isEqualTo(404);
        assertThat(response.getMessage()).contains("не найдена");
    }

    @Test
    @DisplayName("deleteProblemById должен вернуть 404 если дефолтный")
    void deleteProblemById_shouldReturnNotFoundIfDefault() {
        ProblemTypeEntity entity = new ProblemTypeEntity();
        entity.setId(1);
        entity.setCode("DEF");
        entity.setDefault(true);

        when(problemTypeRepository.findById(1)).thenReturn(Optional.of(entity));

        ResponseDTO response = service.deleteProblemById(1);

        assertThat(response.getStatus().value()).isEqualTo(404);
        assertThat(response.getMessage()).contains("дефолтную");
    }

    @Test
    @DisplayName("deleteProblemById должен обновить и удалить")
    void deleteProblemById_shouldUpdateAndDelete() {
        ProblemTypeEntity toDelete = new ProblemTypeEntity();
        toDelete.setId(1);
        toDelete.setCode("OLD");
        toDelete.setDefault(false);

        ProblemTypeEntity def = new ProblemTypeEntity();
        def.setCode("DEFAULT");

        when(problemTypeRepository.findById(1)).thenReturn(Optional.of(toDelete));
        when(problemTypeRepository.findDefaultProblem()).thenReturn(def);
        when(eventRepository.updateProblemForMarkers("OLD", "DEFAULT")).thenReturn(2);

        ResponseDTO response = service.deleteProblemById(1);

        assertThat(response.getStatus().value()).isEqualTo(200);
        assertThat(response.getMessage()).contains("удалена");
        verify(problemTypeRepository).deleteById(1);
    }
}
