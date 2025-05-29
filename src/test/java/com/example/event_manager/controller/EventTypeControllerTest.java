package com.example.event_manager.controller;

import com.example.event_manager.entity.EventTypeEntity;
import com.example.event_manager.model.ResponseDTO;
import com.example.event_manager.model.eventType.EventTypeDTO;
import com.example.event_manager.service.EventTypeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.http.MediaType;

@WebMvcTest(EventTypeController.class)
@Import(EventTypeControllerTest.MockConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class EventTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventTypeService eventTypeService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public EventTypeService eventTypeService() {
            return Mockito.mock(EventTypeService.class);
        }
    }

    @Test
    void saveNewEventType_shouldReturnCreatedEntity() throws Exception {
        EventTypeDTO dto = new EventTypeDTO();
        dto.setCode("CONFERENCE");

        EventTypeEntity entity = new EventTypeEntity();
        entity.setCode("CONFERENCE");

        when(eventTypeService.saveNewEventType(any(EventTypeDTO.class))).thenReturn(entity);

        mockMvc.perform(post("/event/type/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("CONFERENCE"));

        verify(eventTypeService).saveNewEventType(any(EventTypeDTO.class));
    }

    @Test
    void getAllEventTypes_shouldReturnList() throws Exception {
        EventTypeEntity a = new EventTypeEntity();
        a.setId(1);
        a.setCode("A");

        EventTypeEntity b = new EventTypeEntity();
        b.setId(2);
        b.setCode("B");

        when(eventTypeService.getAllEventTypes()).thenReturn(List.of(a, b));

        mockMvc.perform(get("/event/type/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(eventTypeService).getAllEventTypes();
    }

    @Test
    void getEventTypeById_shouldReturnOne() throws Exception {
        EventTypeEntity entity = new EventTypeEntity();
        entity.setId(1);
        entity.setCode("MEETING");

        when(eventTypeService.getEventTypeById(1)).thenReturn(Optional.of(entity));

        mockMvc.perform(get("/event/type/get/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("MEETING"));

        verify(eventTypeService).getEventTypeById(1);
    }

    @Test
    void deleteEventTypeById_shouldReturnResponse() throws Exception {
        ResponseDTO response = new ResponseDTO(HttpStatus.OK, "Тип удалён");

        when(eventTypeService.deleteEventTypeById(1)).thenReturn(response);

        mockMvc.perform(delete("/event/type/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Тип удалён"));

        verify(eventTypeService).deleteEventTypeById(1);
    }
}
