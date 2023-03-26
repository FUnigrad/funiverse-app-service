package com.unigrad.funiverseappservice.service;

import com.unigrad.funiverseappservice.entity.academic.Combo;
import com.unigrad.funiverseappservice.payload.DTO.ComboPlanDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IComboService extends IBaseService<Combo, Long> {

    List<Combo> getAllByCurriculumId(Long id);

    Combo addComboToCurriculum(ComboPlanDTO comboPlanDTO);
}