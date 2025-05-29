package com.example.event_manager.model.status;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Сущность статуса")
public class StatusDTO {

    @NotNull
    @Size(max = 50)
    @Schema(description = "Название статуса", example = "В РАБОТЕ")
    private String code;

    @Size(max = 256)
    @Schema(description = "Описание статуса", example = "работаем, работаем")
    private String description;
}
