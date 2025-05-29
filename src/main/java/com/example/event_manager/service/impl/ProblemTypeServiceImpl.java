package com.example.event_manager.service.impl;

import com.example.event_manager.entity.ProblemTypeEntity;
import com.example.event_manager.model.ResponseDTO;
import com.example.event_manager.model.problemType.ProblemTypeDTO;
import com.example.event_manager.repository.EventRepository;
import com.example.event_manager.repository.ProblemTypeRepository;
import com.example.event_manager.service.ProblemTypeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProblemTypeServiceImpl implements ProblemTypeService {

    @Autowired
    private final ProblemTypeRepository problemTypeRepository;

    @Autowired
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public ProblemTypeEntity saveNewProblem(ProblemTypeDTO problemTypeDTO) {

        ProblemTypeEntity problemTypeEntity = new ProblemTypeEntity();
        problemTypeEntity.setCode(problemTypeDTO.getCode());
        problemTypeEntity.setDescription(problemTypeDTO.getDescription());

        return problemTypeRepository.save(problemTypeEntity);
    }

    @Override
    public List<ProblemTypeEntity> getAllProblems() {
        return problemTypeRepository.findAll();
    }

    @Override
    public Optional<ProblemTypeEntity> getProblemById(Integer id) {
        return problemTypeRepository.findById(id);
    }

    @Override
    @Transactional
    public ResponseDTO deleteProblemById(Integer id) {
        Optional<ProblemTypeEntity> problemTypeEntity = problemTypeRepository.findById(id);
        ResponseDTO response;

        if (problemTypeEntity.isPresent()) {
            if (problemTypeEntity.get().isDefault()) {
                response = new ResponseDTO(HttpStatus.NOT_FOUND, "Нельзя удалить дефолтную проблему");
            }
            else {
                ProblemTypeEntity defaultProblem = problemTypeRepository.findDefaultProblem();
                int updateCount = eventRepository.updateProblemForMarkers(problemTypeEntity.get().getCode(), defaultProblem.getCode());

                problemTypeRepository.deleteById(id);
                response = new ResponseDTO(HttpStatus.OK, "Проблема удалена!");
            }
        }
        else {
            response = new ResponseDTO(HttpStatus.NOT_FOUND, "Проблема c айди " + id + " не найдена!");
        }

        return response;
    }
}
