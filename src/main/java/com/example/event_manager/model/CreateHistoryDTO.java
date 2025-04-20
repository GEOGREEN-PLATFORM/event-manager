package com.example.event_manager.model;

import com.example.event_manager.model.image.ImageDTO;
import com.example.event_manager.util.serialization.FlexibleInstantDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Сущность записи по работам в рамках мероптиятия")
public class CreateHistoryDTO {

    @NotNull
    @Schema(description = "Тип истории по мероприятию", example = "Наблюдение")
    private String recordType;

    @PastOrPresent
    @JsonDeserialize(using = FlexibleInstantDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    @Schema(description = "Дата работ (по умолчанию текущая дата)", example = "2027-04-10T12:00:00+03:00")
    private Instant recordDate;

    @Schema(description = "Описание работ", example = "тут много борщевика")
    private String description;

    @Schema(description = "Лист айди фотографий борщевика")
    private List<ImageDTO> photos;

    @NotNull
    @Schema(description = "Айди оператора мероприятия", example = "12f0887f-6b1d-4ee0-aba9-9e006bc4745e")
    private UUID operatorId;
}
