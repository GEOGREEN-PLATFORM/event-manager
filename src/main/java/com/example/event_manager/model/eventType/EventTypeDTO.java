package com.example.event_manager.model.eventType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Сущность типа мероприятия")
public class EventTypeDTO {

    @NotNull
    @Size(max = 50)
    @Schema(description = "Название типа мероприятия", example = "Наблюдение")
    private String code;

    @Size(max = 256)
    @Schema(description = "Описание типа", example = "Мы смотрим и наблюдаем, вот так вот")
    private String description;
}
