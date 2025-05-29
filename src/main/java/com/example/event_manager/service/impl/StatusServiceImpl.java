package com.example.event_manager.service.impl;

import com.example.event_manager.entity.StatusEntity;
import com.example.event_manager.model.ResponseDTO;
import com.example.event_manager.model.status.StatusDTO;
import com.example.event_manager.repository.EventRepository;
import com.example.event_manager.repository.StatusRepository;
import com.example.event_manager.service.StatusService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {

    @Autowired
    private final StatusRepository statusRepository;

    @Autowired
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public StatusEntity saveNewStatus(StatusDTO statusDTO) {

        StatusEntity statusEntity = new StatusEntity();
        statusEntity.setCode(statusDTO.getCode());
        statusEntity.setDescription(statusDTO.getDescription());

        return statusRepository.save(statusEntity);
    }

    @Override
    public List<StatusEntity> getAllStatuses() {
        return statusRepository.findAll();
    }

    @Override
    public Optional<StatusEntity> findStatusById(Integer id) {
        return statusRepository.findById(id);
    }

    @Override
    @Transactional
    public ResponseDTO deleteStatus(Integer id) {
        Optional<StatusEntity> statusEntity = statusRepository.findById(id);
        ResponseDTO response;

        if (statusEntity.isPresent()) {

            if (statusEntity.get().isDefault()) {
                response = new ResponseDTO(HttpStatus.NOT_FOUND, "Нельзя удалить дефолтный статус");
            }
            else {
                StatusEntity defaultStatus = statusRepository.findDefaultStatus();
                int updateCount = eventRepository.updateStatusForEvents(statusEntity.get().getCode(), defaultStatus.getCode());

                statusRepository.deleteById(id);
                response = new ResponseDTO(HttpStatus.OK, "Статус удалён!");
            }
        }
        else {
            response = new ResponseDTO(HttpStatus.NOT_FOUND, "Статус c айди " + id + " не найден!");
        }

        return response;
    }
}
