package com.example.event_manager.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Schema(description = "Сущность для обновления мероприятия")
public class UpdateEventDTO {

    @Column
    @Pattern(regexp = "ПЛАНИРУЕТСЯ|В РАБОТЕ|ПРИОСТАНОВЛЕНО|ЗАВЕРШЕНО", message = "Status must be one of: ПЛАНИРУЕТСЯ, В РАБОТЕ, ПРИОСТАНОВЛЕНО, ЗАВЕРШЕНО")
    @Schema(description = "Статус мероприятия", allowableValues = {"ПЛАНИРУЕТСЯ", "В РАБОТЕ", "ПРИОСТАНОВЛЕНО", "ЗАВЕРШЕНО"})
    private String status;

    @Column
    @PastOrPresent
    @Schema(description = "Дата завершения мероприятия", example = "2025-02-19")
    private LocalDate endDate;

}
