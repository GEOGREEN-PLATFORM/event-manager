package com.example.event_manager.entity;

import com.example.event_manager.converter.ImageListConverter;
import com.example.event_manager.model.image.ImageDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "event_history")
@Data
public class EventHistoryEntity {

    @Id
    @GeneratedValue(generator = "uuid-generator")
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    @NotNull
    private UUID id;

    @NotNull
    @Column(name = "event_id")
    private UUID eventId;

    @NotNull
    @Column(name = "record_date")
    private Instant recordDate;

    @NotNull
    @Column(name = "record_type")
    @JoinColumn(name = "event_type", referencedColumnName = "code")
    private String recordType;

    @NotNull
    @Column(name = "description")
    private String description;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "photos", columnDefinition = "jsonb")
    @Convert(converter = ImageListConverter.class)
    private List<ImageDTO> photos;

    @Column(name = "operator_name")
    @Size(max = 50)
    private String operatorName;

    @Column(name = "operator_id")
    private UUID operatorId;

    @Column(name = "create_date", nullable = false, updatable = false)
    private Instant createDate;


}
