package com.example.event_manager.controller;

import com.example.event_manager.entity.StatusEntity;
import com.example.event_manager.model.ResponseDTO;
import com.example.event_manager.model.status.StatusDTO;
import com.example.event_manager.service.StatusService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;

@WebMvcTest(StatusController.class)
@Import(StatusControllerTest.MockConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class StatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StatusService statusService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public StatusService statusService() {
            return Mockito.mock(StatusService.class);
        }
    }

    @Test
    void saveNewStatus_shouldReturnCreatedStatus() throws Exception {
        StatusDTO dto = new StatusDTO();
        dto.setCode("IN_PROGRESS");

        StatusEntity entity = new StatusEntity();
        entity.setCode("IN_PROGRESS");

        when(statusService.saveNewStatus(any(StatusDTO.class))).thenReturn(entity);

        mockMvc.perform(post("/event/status/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("IN_PROGRESS"));

        verify(statusService).saveNewStatus(any(StatusDTO.class));
    }

    @Test
    void getAllStatuses_shouldReturnList() throws Exception {
        StatusEntity s1 = new StatusEntity();
        s1.setId(1);
        s1.setCode("NEW");

        StatusEntity s2 = new StatusEntity();
        s2.setId(2);
        s2.setCode("CLOSED");

        when(statusService.getAllStatuses()).thenReturn(List.of(s1, s2));

        mockMvc.perform(get("/event/status/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(statusService).getAllStatuses();
    }

    @Test
    void getStatusById_shouldReturnStatus() throws Exception {
        StatusEntity entity = new StatusEntity();
        entity.setId(1);
        entity.setCode("DONE");

        when(statusService.findStatusById(1)).thenReturn(Optional.of(entity));

        mockMvc.perform(get("/event/status/get/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("DONE"));

        verify(statusService).findStatusById(1);
    }

    @Test
    void deleteStatus_shouldReturnResponseDTO() throws Exception {
        ResponseDTO response = new ResponseDTO(HttpStatus.OK, "Статус удалён");

        when(statusService.deleteStatus(1)).thenReturn(response);

        mockMvc.perform(delete("/event/status/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Статус удалён"));

        verify(statusService).deleteStatus(1);
    }
}
