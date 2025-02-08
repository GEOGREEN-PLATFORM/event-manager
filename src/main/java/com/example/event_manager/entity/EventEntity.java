package com.example.event_manager.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "event")
@Data
public class EventEntity {

    @Id
    @GeneratedValue(generator = "uuid-generator")
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    @NotNull
    private UUID id;

    @NotNull
    @Column
    private UUID sourceId;

    @NotNull
    @Column
    @PastOrPresent
    private LocalDate startDate;

    @Column
    @PastOrPresent
    private LocalDate endDate;

    @NotNull
    @Column
    @PastOrPresent
    private LocalDate lastUpdateDate;

    @NotNull
    @Column
    @Pattern(regexp = "ПЛАНИРУЕТСЯ|В РАБОТЕ|ПРИОСТАНОВЛЕНО|ЗАВЕРШЕНО", message = "Status must be one of: ПЛАНИРУЕТСЯ, В РАБОТЕ, ПРИОСТАНОВЛЕНО, ЗАВЕРШЕНО")
    private String status;

    @NotNull
    @Column
    @Pattern(regexp = "УСТРАНЕНИЕ БОРЩЕВИКА|УСТРЕНЕНИЕ ПОСЛЕДСТВИЙ ПОЖАРА|СВАЛКА", message = "Event type must be one of: УСТРАНЕНИЕ БОРЩЕВИКА, УСТРЕНЕНИЕ ПОСЛЕДСТВИЙ ПОЖАРА, СВАЛКА")
    private String eventType;

    @NotNull
    @Column
    private String description;
}
