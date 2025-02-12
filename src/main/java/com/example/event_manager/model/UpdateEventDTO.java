package com.example.event_manager.model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UpdateEventDTO {

    @Column
    @Pattern(regexp = "ПЛАНИРУЕТСЯ|В РАБОТЕ|ПРИОСТАНОВЛЕНО|ЗАВЕРШЕНО", message = "Status must be one of: ПЛАНИРУЕТСЯ, В РАБОТЕ, ПРИОСТАНОВЛЕНО, ЗАВЕРШЕНО")
    private String status;

    @Column
    @PastOrPresent
    private LocalDate endDate;

}
