package com.example.event_manager.model;

import com.example.event_manager.util.serialization.FlexibleInstantDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Сущность для обновления мероприятия")
public class UpdateEventDTO {

    @Schema(description = "Статус мероприятия", example = "СОЗДАНО")
    private String statusCode;

    @Schema(description = "Название мероприятия", example = "Название")
    private String name;

    @JsonDeserialize(using = FlexibleInstantDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    @Schema(description = "Дата завершения мероприятия", example = "2027-04-10T12:00:00+03:00")
    private Instant endDate;

    @Schema(description = "Описание мероприятия", example = "тут много борщевика")
    private String description;

    @Schema(description = "Айди оператора мероприятия", example = "12f0887f-6b1d-4ee0-aba9-9e006bc4745e")
    private UUID operatorId;

}
