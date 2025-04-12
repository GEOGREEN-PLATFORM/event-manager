package com.example.event_manager.service;

import com.example.event_manager.entity.StatusEntity;
import com.example.event_manager.model.ResponseDTO;
import com.example.event_manager.model.status.StatusDTO;

import java.util.List;
import java.util.Optional;

public interface StatusService {
    StatusEntity saveNewStatus(StatusDTO statusDTO);

    List<StatusEntity> getAllStatuses();

    Optional<StatusEntity> findStatusById(Integer id);

    ResponseDTO deleteStatus(Integer id);
}
