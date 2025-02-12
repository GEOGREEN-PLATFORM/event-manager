package com.example.event_manager.model;

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
public class CreateHistoryDTO {

    @NotNull
    @Column
    @Pattern(regexp = "ПРОВЕДЕННАЯ РАБОТА|СНЯТИЕ СОСТОЯНИЯ", message = "Record type must be one of: ПРОВЕДЕННАЯ РАБОТА, СНЯТИЕ СОСТОЯНИЯ")
    private String recordType;

    @Column
    @PastOrPresent
    private LocalDate recordDate;

    @Column
    private String description;

    @Column
    private List<UUID> photos;
}
