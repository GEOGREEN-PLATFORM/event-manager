package com.example.event_manager.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "event_history")
@Data
public class EventHistory {

    @Id
    @GeneratedValue(generator = "uuid-generator")
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    @NotNull
    private UUID id;

    @NotNull
    @Column
    private UUID eventId;

    @NotNull
    @Column
    @PastOrPresent
    private LocalDate recordDate;

    @NotNull
    @Column
    @Pattern(regexp = "ПРОВЕДЕННАЯ РАБОТА|СНЯТИЕ СОСТОЯНИЯ", message = "Record type must be one of: ПРОВЕДЕННАЯ РАБОТА, СНЯТИЕ СОСТОЯНИЯ")
    private String recordType;

    @NotNull
    @Column
    private String description;

    @Column
    private List<UUID> photos;
}
