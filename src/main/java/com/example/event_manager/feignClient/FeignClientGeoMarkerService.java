package com.example.event_manager.feignClient;

import com.example.event_manager.model.geoMarker.GeoMarkerDTO;
import com.example.event_manager.model.geoMarker.RelatedTaskDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name="geo-marker", url="${geospatial.server.host}")
public interface FeignClientGeoMarkerService {
    @GetMapping("/geo/info/{geoPointId}")
    GeoMarkerDTO getGeoPointById(@RequestHeader("Authorization") String token, @PathVariable UUID geoPointId);

    @PostMapping("geo/info/related-task/{geoPointId}")
    void postRelatedTask(@RequestHeader("Authorization") String token, @PathVariable UUID geoPointId, @RequestBody RelatedTaskDTO relatedTaskDTO);

    @PatchMapping("/geo/info/{geoPointId}")
    GeoMarkerDTO updateGeoPoint(@RequestHeader("Authorization") String token, @PathVariable UUID geoPointId, @RequestBody GeoMarkerDTO geoMarkerDTO);
}
