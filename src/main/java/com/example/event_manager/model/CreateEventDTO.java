package com.example.event_manager.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@Schema(description = "Сущность нового мероптиятия")
public class CreateEventDTO {

    @NotNull
    @Schema(description = "Айди очага", example = "d2f0887f-6b1d-4ee0-aba9-9e006bc4745e")
    private UUID geoPointId;

    @NotNull
    @Size(max = 50)
    @Schema(description = "Тип мероприятия", example = "Наблюдение")
    private String eventType;

    @NotNull
    @Size(max = 50)
    @Schema(description = "Тип экологической проблемы", example = "Борщевик")
    private String problemAreaType;

    @NotNull
    @Schema(description = "Название мероприятия", example = "Название")
    private String name;

    @NotNull
    @Schema(description = "Описание мероприятия", example = "тут много борщевика")
    private String description;

    @NotNull
    @Schema(description = "Дата начала мероприятия", example = "2027-04-10T12:00:00+03:00")
    private Instant startDate;

    @Schema(description = "Планируемая дата завершения мероприятия", example = "2027-04-10T12:00:00+03:00")
    private Instant endDate;

    @NotNull
    @Schema(description = "Айди автора мероприятия", example = "12f0887f-6b1d-4ee0-aba9-9e006bc4745e")
    private UUID authorId;

    @Schema(description = "Айди оператора мероприятия", example = "12f0887f-6b1d-4ee0-aba9-9e006bc4745e")
    private UUID operatorId;
}
