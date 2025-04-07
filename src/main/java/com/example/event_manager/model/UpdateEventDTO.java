package com.example.event_manager.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@Schema(description = "Сущность для обновления мероприятия")
public class UpdateEventDTO {

    @Pattern(regexp = "ПЛАНИРУЕТСЯ|В РАБОТЕ|ПРИОСТАНОВЛЕНО|ЗАВЕРШЕНО", message = "Status must be one of: ПЛАНИРУЕТСЯ, В РАБОТЕ, ПРИОСТАНОВЛЕНО, ЗАВЕРШЕНО")
    @Schema(description = "Статус мероприятия", allowableValues = {"ПЛАНИРУЕТСЯ", "В РАБОТЕ", "ПРИОСТАНОВЛЕНО", "ЗАВЕРШЕНО"})
    private String status;

    @Schema(description = "Название мероприятия", example = "Название")
    private String name;

    @Schema(description = "Дата завершения мероприятия", example = "2025-02-19")
    private LocalDate endDate;

    @Schema(description = "Описание мероприятия", example = "тут много борщевика")
    private String description;

    @Schema(description = "Айди оператора мероприятия", example = "12f0887f-6b1d-4ee0-aba9-9e006bc4745e")
    private UUID operatorId;

}
