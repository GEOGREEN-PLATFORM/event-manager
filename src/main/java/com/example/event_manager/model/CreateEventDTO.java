package com.example.event_manager.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CreateEventDTO {

    @NotNull
    private UUID sourceId;

    @NotNull
    @Pattern(regexp = "УСТРАНЕНИЕ БОРЩЕВИКА|УСТРЕНЕНИЕ ПОСЛЕДСТВИЙ ПОЖАРА|СВАЛКА", message = "Event type must be one of: УСТРАНЕНИЕ БОРЩЕВИКА, УСТРЕНЕНИЕ ПОСЛЕДСТВИЙ ПОЖАРА, СВАЛКА")
    private String eventType;

    @NotNull
    private String description;

    private LocalDate startDate;
}
