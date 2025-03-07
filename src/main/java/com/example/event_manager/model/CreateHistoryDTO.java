package com.example.event_manager.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@Schema(description = "Сущность записи по работам в рамках мероптиятия")
public class CreateHistoryDTO {

    @NotNull
    @Column
    @Pattern(regexp = "ПРОВЕДЕННАЯ РАБОТА|СНЯТИЕ СОСТОЯНИЯ", message = "Record type must be one of: ПРОВЕДЕННАЯ РАБОТА, СНЯТИЕ СОСТОЯНИЯ")
    @Schema(description = "Тип истории по мероприятию", allowableValues = {"ПРОВЕДЕННАЯ РАБОТА", "СНЯТИЕ СОСТОЯНИЯ"})
    private String recordType;

    @Column
    @PastOrPresent
    @Schema(description = "Дата работ", example = "2025-02-19")
    private LocalDate recordDate;

    @Column
    @Schema(description = "Описание работ", example = "тут много борщевика")
    private String description;

    @Column
    @Schema(description = "Лист айди фотографий борщевика", example = "[7632b748-02bf-444b-bb95-1a4e6e1cffc5]")
    private List<UUID> photos;
}
