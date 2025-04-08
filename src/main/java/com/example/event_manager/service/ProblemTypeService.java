package com.example.event_manager.service;

import com.example.event_manager.entity.ProblemTypeEntity;
import com.example.event_manager.model.ResponseDTO;
import com.example.event_manager.model.problemType.ProblemTypeDTO;

import java.util.List;
import java.util.Optional;

public interface ProblemTypeService {

    ProblemTypeEntity saveNewProblem(ProblemTypeDTO problemTypeDTO);

    List<ProblemTypeEntity> getAllProblems();

    Optional<ProblemTypeEntity> getProblemById(Integer id);

    ResponseDTO deleteProblemById(Integer id);
}
