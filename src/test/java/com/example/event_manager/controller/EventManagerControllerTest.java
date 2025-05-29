package com.example.event_manager.controller;

import com.example.event_manager.entity.EventEntity;
import com.example.event_manager.entity.EventHistoryEntity;
import com.example.event_manager.model.CreateEventDTO;
import com.example.event_manager.model.CreateHistoryDTO;
import com.example.event_manager.model.UpdateEventDTO;
import com.example.event_manager.service.EventManagerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventManagerController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(EventManagerControllerTest.MockConfig.class)
class EventManagerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventManagerService eventManagerService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public EventManagerService eventManagerService() {
            return Mockito.mock(EventManagerService.class);
        }
    }

    @Test
    void saveNewEvent_shouldReturnCreatedEvent() throws Exception {
        CreateEventDTO dto = new CreateEventDTO();
        dto.setGeoPointId(UUID.randomUUID());

        EventEntity event = new EventEntity();
        event.setId(UUID.randomUUID());

        when(eventManagerService.createNewEvent(any(), anyString())).thenReturn(event);

        mockMvc.perform(post("/event/create")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(eventManagerService).createNewEvent(any(), anyString());
    }

    @Test
    void saveNewHistory_shouldReturnCreatedHistory() throws Exception {
        CreateHistoryDTO dto = new CreateHistoryDTO();
        UUID eventId = UUID.randomUUID();
        EventHistoryEntity history = new EventHistoryEntity();

        when(eventManagerService.createNewHistory(any(), eq(eventId), anyString())).thenReturn(history);

        mockMvc.perform(post("/event/{eventId}/history", eventId)
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(eventManagerService).createNewHistory(any(), eq(eventId), anyString());
    }

    @Test
    void getEventById_shouldReturnEvent() throws Exception {
        UUID eventId = UUID.randomUUID();
        EventEntity event = new EventEntity();
        event.setId(eventId);

        when(eventManagerService.getEventById(eventId)).thenReturn(event);

        mockMvc.perform(get("/event/{eventId}", eventId))
                .andExpect(status().isOk());

        verify(eventManagerService).getEventById(eventId);
    }

    @Test
    void getAllEvents_shouldReturnPage() throws Exception {
        EventEntity e1 = new EventEntity();
        e1.setId(UUID.randomUUID());

        Page<EventEntity> page = new PageImpl<>(List.of(e1));

        when(eventManagerService.getAllEvents(
                anyInt(), anyInt(), any(), any(), any(), any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(page);

        mockMvc.perform(get("/event/getAll"))
                .andExpect(status().isOk());

        verify(eventManagerService).getAllEvents(
                anyInt(), anyInt(), any(), any(), any(), any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void getEventHistory_shouldReturnPage() throws Exception {
        UUID eventId = UUID.randomUUID();
        Page<EventHistoryEntity> page = new PageImpl<>(List.of(new EventHistoryEntity()));

        when(eventManagerService.getEventHistory(eventId, 0, 10)).thenReturn(page);

        mockMvc.perform(get("/event/{eventId}/history", eventId))
                .andExpect(status().isOk());

        verify(eventManagerService).getEventHistory(eventId, 0, 10);
    }

    @Test
    void updateEvent_shouldReturnUpdatedEvent() throws Exception {
        UUID eventId = UUID.randomUUID();
        UpdateEventDTO dto = new UpdateEventDTO();

        EventEntity updated = new EventEntity();
        updated.setId(eventId);

        when(eventManagerService.updateEvent(any(), eq(eventId), anyString())).thenReturn(updated);

        mockMvc.perform(patch("/event/{eventId}", eventId)
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(eventManagerService).updateEvent(any(), eq(eventId), anyString());
    }

    @Test
    void deleteEventHistory_shouldReturnResponse() throws Exception {
        UUID eventId = UUID.randomUUID();
        UUID historyId = UUID.randomUUID();

        mockMvc.perform(delete("/event/{eventId}/history/{historyId}", eventId, historyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("История удалена"));

        verify(eventManagerService).deleteHistory(eventId, historyId);
    }

    @Test
    void deleteEvent_shouldReturnResponse() throws Exception {
        UUID eventId = UUID.randomUUID();

        mockMvc.perform(delete("/event/{eventId}", eventId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Мероприятие удалено"));

        verify(eventManagerService).deleteEvent(eventId);
    }
}
