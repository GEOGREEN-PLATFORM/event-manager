package com.example.event_manager.controller;

import com.example.event_manager.entity.ProblemTypeEntity;
import com.example.event_manager.model.ResponseDTO;
import com.example.event_manager.model.problemType.ProblemTypeDTO;
import com.example.event_manager.service.ProblemTypeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;

@WebMvcTest(ProblemTypeController.class)
@Import(ProblemTypeControllerTest.MockConfig.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProblemTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProblemTypeService problemTypeService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public ProblemTypeService problemTypeService() {
            return Mockito.mock(ProblemTypeService.class);
        }
    }

    @Test
    void saveNewProblem_shouldReturnCreated() throws Exception {
        ProblemTypeDTO dto = new ProblemTypeDTO();
        dto.setCode("SOIL");

        ProblemTypeEntity entity = new ProblemTypeEntity();
        entity.setCode("SOIL");

        when(problemTypeService.saveNewProblem(any(ProblemTypeDTO.class))).thenReturn(entity);

        mockMvc.perform(post("/event/problemType/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SOIL"));

        verify(problemTypeService).saveNewProblem(any(ProblemTypeDTO.class));
    }

    @Test
    void getAllProblems_shouldReturnList() throws Exception {
        ProblemTypeEntity a = new ProblemTypeEntity();
        a.setId(1);
        a.setCode("A");

        ProblemTypeEntity b = new ProblemTypeEntity();
        b.setId(2);
        b.setCode("B");

        when(problemTypeService.getAllProblems()).thenReturn(List.of(a, b));

        mockMvc.perform(get("/event/problemType/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(problemTypeService).getAllProblems();
    }

    @Test
    void getProblemById_shouldReturnEntity() throws Exception {
        ProblemTypeEntity entity = new ProblemTypeEntity();
        entity.setId(1);
        entity.setCode("AIR");

        when(problemTypeService.getProblemById(1)).thenReturn(Optional.of(entity));

        mockMvc.perform(get("/event/problemType/get/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("AIR"));

        verify(problemTypeService).getProblemById(1);
    }

    @Test
    void deleteProblemById_shouldReturnResponseDTO() throws Exception {
        ResponseDTO response = new ResponseDTO(HttpStatus.OK, "Удалено");

        when(problemTypeService.deleteProblemById(1)).thenReturn(response);

        mockMvc.perform(delete("/event/problemType/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Удалено"));

        verify(problemTypeService).deleteProblemById(1);
    }
}
