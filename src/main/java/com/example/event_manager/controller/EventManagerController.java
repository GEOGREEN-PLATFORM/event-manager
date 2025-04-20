package com.example.event_manager.controller;

import com.example.event_manager.entity.EventEntity;
import com.example.event_manager.entity.EventHistoryEntity;
import com.example.event_manager.model.CreateEventDTO;
import com.example.event_manager.model.CreateHistoryDTO;
import com.example.event_manager.model.ResponseDTO;
import com.example.event_manager.model.UpdateEventDTO;
import com.example.event_manager.service.EventManagerService;
import com.example.event_manager.util.pagination.SimplifiedPageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.example.event_manager.util.AuthorizationStringUtil.*;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@SecurityRequirement(name = AUTHORIZATION)
@Tag(name = "Мероприятия и работы", description = "Позволяет планировать и управлять мероприятиями и проведенными работами")
public class EventManagerController {

    @Autowired
    private EventManagerService eventManagerService;
    private static final Logger logger = LoggerFactory.getLogger(EventManagerController.class);

    @PostMapping
    @Operation(
            summary = "Создание нового мероприятия",
            description = "Записывает в базу данных новое мероприятие"
    )
    @RolesAllowed({ADMIN, OPERATOR})
    public EventEntity saveNewEvent(@RequestBody @Parameter(description = "Сущность нового мероптиятия", required = true) CreateEventDTO createEventDTO) {
        logger.info("Пришел запрос на создание мероприятие для очага - {}", createEventDTO.getGeoPointId());
        return eventManagerService.createNewEvent(createEventDTO);
    }

    @PostMapping("/{eventId}/history")
    @Operation(
            summary = "Создание новой истории в рамках мероприятия",
            description = "Записывает в базу данных новую историю о работах по мероприяитию"
    )
    @RolesAllowed({ADMIN, OPERATOR})
    @ApiResponse(responseCode = "404", description = "Event with id ... not found!")
    public EventHistoryEntity saveNewHistory(@RequestBody @Parameter(description = "Сущность записи по работам в рамках мероптиятия", required = true) CreateHistoryDTO createHistoryDTO, @PathVariable @Parameter(description = "Айди мероптиятия", required = true) UUID eventId) {
        logger.info("Пришел запрос на добавление новой истории в мероприятие с айди - {}", eventId);
        return eventManagerService.createNewHistory(createHistoryDTO, eventId);
    }

    @GetMapping("/{eventId}")
    @Operation(
            summary = "Поулчение мероприятия по айди"
    )
    public EventEntity getEventById(@PathVariable @Parameter(description = "Айди мероптиятия", required = true) UUID eventId) {
        logger.info("Пришел запрос на получение мероприятия по айди - {}", eventId);
        return eventManagerService.getEventById(eventId);
    }

    @GetMapping("/getAll")
    @Operation(
            summary = "Получение всех мероприятий"
    )
    public SimplifiedPageResponse<EventEntity> getAllEvents(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        Page<EventEntity> result  = eventManagerService.getAllEvents(page, size);
        return new SimplifiedPageResponse<>(result);
    }

    @GetMapping("/{eventId}/history")
    @Operation(
            summary = "Поулчение истории мероприятия",
            description = "Возвращает всю историю по мероприятию"
    )
    @ApiResponse(responseCode = "404", description = "Event with id ... not found!")
    public SimplifiedPageResponse<EventHistoryEntity> getEventHistory(@PathVariable @Parameter(description = "Айди мероптиятия", required = true) UUID eventId,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
        logger.info("Пришел запрос на получение истории мероприятия с айди - {}", eventId);
        Page<EventHistoryEntity> result = eventManagerService.getEventHistory(eventId, page, size);
        return new SimplifiedPageResponse<>(result);
    }

    @PatchMapping("/{eventId}")
    @Operation(
            summary = "Обновление мероприятия",
            description = "Обновляет в базе данных существующее мероприятие"
    )
    @RolesAllowed({ADMIN, OPERATOR})
    @ApiResponse(responseCode = "404", description = "Event with id ... not found!")
    public EventEntity updateEvent(@RequestBody @Parameter(description = "Сущность для обновления мероприятия", required = true) UpdateEventDTO updateEventDTO, @PathVariable @Parameter(description = "Айди мероптиятия", required = true) UUID eventId) {
        logger.info("Пришел запрос на обновление мероприятия с айди - {}", eventId);
        return eventManagerService.updateEvent(updateEventDTO, eventId);
    }

    @DeleteMapping("/{eventId}/history/{historyId}")
    @Operation(
            summary = "Удаление одной истории по мероприятию"
    )
    @RolesAllowed({ADMIN, OPERATOR})
    @ApiResponse(responseCode = "404", description = "Event with id ... not found!")
    public ResponseDTO deleteEventHistory(@PathVariable @Parameter(description = "Айди мероптиятия", required = true) UUID eventId, @PathVariable @Parameter(description = "Айди истории по мероптиятию", required = true) UUID historyId) {
        eventManagerService.deleteHistory(eventId, historyId);
        return new ResponseDTO(HttpStatus.OK, "История удалена");
    }

    @DeleteMapping("/{eventId}")
    @Operation(
            summary = "Удаление мероприятия",
            description = "Удаляет мероприятие и все привязанные к нему истории"
    )
    @RolesAllowed({ADMIN, OPERATOR})
    @ApiResponse(responseCode = "404", description = "Event with id ... not found!")
    public  ResponseDTO deleteEvent(@PathVariable @Parameter(description = "Айди мероптиятия", required = true) UUID eventId) {
        eventManagerService.deleteEvent(eventId);
        return new ResponseDTO(HttpStatus.OK, "Мероприятие удалено");
    }
}
