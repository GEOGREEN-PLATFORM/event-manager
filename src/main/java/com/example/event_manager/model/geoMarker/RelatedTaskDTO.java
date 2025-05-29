package com.example.event_manager.model.geoMarker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelatedTaskDTO {

    private UUID relatedTaskId;
}
