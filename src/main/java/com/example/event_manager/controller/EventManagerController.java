package com.example.event_manager.controller;

import com.example.event_manager.entity.EventEntity;
import com.example.event_manager.entity.EventHistoryEntity;
import com.example.event_manager.model.CreateEventDTO;
import com.example.event_manager.model.CreateHistoryDTO;
import com.example.event_manager.model.ResponseDTO;
import com.example.event_manager.model.UpdateEventDTO;
import com.example.event_manager.service.EventManagerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventManagerController {

    @Autowired
    private EventManagerService eventManagerService;
    private static final Logger logger = LoggerFactory.getLogger(EventManagerController.class);

    @PostMapping
    public EventEntity saveNewEvent(@RequestBody CreateEventDTO createEventDTO) {
        logger.info("Пришел запрос на создание мероприятие для очага - {}", createEventDTO.getSourceId());
        return eventManagerService.createNewEvent(createEventDTO);
    }

    @PostMapping("/{eventId}/history")
    public EventHistoryEntity saveNewHistory(@RequestBody CreateHistoryDTO createHistoryDTO, @PathVariable UUID eventId) {
        logger.info("Пришел запрос на добавление новой истории в мероприятие с айди - {}", eventId);
        return eventManagerService.createNewHistory(createHistoryDTO, eventId);
    }

    @GetMapping("/{eventId}/history")
    public List<EventHistoryEntity> getEventHistory(@PathVariable UUID eventId) {
        logger.info("Пришел запрос на получение истории мероприятия с айди - {}", eventId);
        return eventManagerService.getEventHistory(eventId);
    }

    @PatchMapping("/{eventId}")
    public EventEntity updateEvent(@RequestBody UpdateEventDTO updateEventDTO, @PathVariable UUID eventId) {
        logger.info("Пришел запрос на обновление мероприятия с айди - {}", eventId);
        return eventManagerService.updateEvent(updateEventDTO, eventId);
    }

    @DeleteMapping("/{eventId}/history/{historyId}")
    public ResponseDTO deleteEventHistory(@PathVariable UUID eventId, @PathVariable UUID historyId) {
        eventManagerService.deleteHistory(eventId, historyId);
        return new ResponseDTO(HttpStatus.OK, "История удалена");
    }

    @DeleteMapping("/{eventId}")
    public  ResponseDTO deleteEvent(@PathVariable UUID eventId) {
        eventManagerService.deleteEvent(eventId);
        return new ResponseDTO(HttpStatus.OK, "Мероприятие удалено");
    }
}
