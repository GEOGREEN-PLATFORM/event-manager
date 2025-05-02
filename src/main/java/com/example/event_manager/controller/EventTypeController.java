package com.example.event_manager.controller;

import com.example.event_manager.entity.EventTypeEntity;
import com.example.event_manager.model.ResponseDTO;
import com.example.event_manager.model.eventType.EventTypeDTO;
import com.example.event_manager.service.EventTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.example.event_manager.util.AuthorizationStringUtil.*;

@RestController
@RequestMapping("/event/type")
@RequiredArgsConstructor
@SecurityRequirement(name = AUTHORIZATION)
@Tag(name = "Тип мероприятия", description = "Позволяет управлять типами мероприятий")
public class EventTypeController {

    @Autowired
    private final EventTypeService eventTypeService;

    private static final Logger logger = LoggerFactory.getLogger(EventTypeController.class);

    @PostMapping("/create")
    @Operation(
            summary = "Создание нового типа мероприятия",
            description = "Записывает в базу данных новый тип мероприятия"
    )
    @RolesAllowed({ADMIN, OPERATOR})
    public EventTypeEntity saveNewEventType(@RequestBody @Parameter(description = "Сущность типа мероприятия", required = true) EventTypeDTO eventTypeDTO) {
        logger.info("Пролучен запрос POST /eventType/create на создание типа мероприятия {}", eventTypeDTO.getCode());
        logger.debug("POST /eventType/create - {}", eventTypeDTO);
        return eventTypeService.saveNewEventType(eventTypeDTO);
    }

    @GetMapping("/getAll")
    @Operation(
            summary = "Получить все типы мероприятий"
    )
    @RolesAllowed({ADMIN, OPERATOR})
    public List<EventTypeEntity> getAllEventTypes() {
        logger.info("Получен запрос GET /eventType/getAll");
        return eventTypeService.getAllEventTypes();
    }

    @GetMapping("/get/{id}")
    @Operation(
            summary = "Получить тип мероприятия по айди"
    )
    @RolesAllowed({ADMIN, OPERATOR})
    public Optional<EventTypeEntity> getEventTypeById(@PathVariable @Parameter(description = "Уникальный айди типа мероприятия", required = true) Integer id) {
        logger.info("Получен запрос GET /eventType/get/{}", id);
        return eventTypeService.getEventTypeById(id);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удаление типа мероприятия"
    )
    @RolesAllowed({ADMIN, OPERATOR})
    public ResponseDTO deleteEventTypeById(@PathVariable @Parameter(description = "Уникальный айди типа мероприятия", required = true) Integer id) {
        logger.info("Получен запрос DELETE /eventType/{}", id);
        return eventTypeService.deleteEventTypeById(id);
    }

}
