package com.unigrad.funiverseappservice.service.impl;

import com.unigrad.funiverseappservice.entity.academic.Combo;
import com.unigrad.funiverseappservice.entity.academic.Curriculum;
import com.unigrad.funiverseappservice.entity.academic.CurriculumPlan;
import com.unigrad.funiverseappservice.payload.ComboPlanDTO;
import com.unigrad.funiverseappservice.repository.IComboRepository;
import com.unigrad.funiverseappservice.service.IComboService;
import com.unigrad.funiverseappservice.service.ICurriculumPlanService;
import com.unigrad.funiverseappservice.service.ISyllabusService;
import com.unigrad.funiverseappservice.specification.EntitySpecification;
import com.unigrad.funiverseappservice.util.DTOConverter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComboService implements IComboService {

    private final IComboRepository comboRepository;

    private final ISyllabusService syllabusService;

    private final ICurriculumPlanService curriculumPlanService;

    private final DTOConverter dtoConverter;

    public ComboService(IComboRepository comboRepository, DTOConverter dtoConverter, ISyllabusService syllabusService, ICurriculumPlanService curriculumPlanService, DTOConverter dtoConverter1) {
        this.comboRepository = comboRepository;
        this.syllabusService = syllabusService;
        this.curriculumPlanService = curriculumPlanService;
        this.dtoConverter = dtoConverter1;
    }

    @Override
    public List<Combo> getAll() {
        return comboRepository.findAll();
    }

    @Override
    public List<Combo> getAllActive() {
        return comboRepository.findAllByActiveIsTrue();
    }

    @Override
    public Optional<Combo> get(Long key) {
        return comboRepository.findById(key);
    }

    @Override
    public Combo save(Combo entity) {

        return comboRepository.save(entity);
    }

    @Override
    public void activate(Long key) {

    }

    @Override
    public void inactivate(Long key) {

    }

    @Override
    public boolean isExist(Long key) {
        return comboRepository.existsById(key);
    }

    @Override
    public List<Combo> search(EntitySpecification<Combo> specification) {
        return comboRepository.findAll(specification);
    }

    @Override
    public List<Combo> getAllByCurriculumId(Long id) {
        return comboRepository.getAllByCurriculumId(id);
    }

    @Override
    public Combo addComboToCurriculum(ComboPlanDTO comboDTO) {

        Optional<Combo> comboOpt = get(comboDTO.getCombo().getId());

        comboDTO.getComboPlans().forEach(comboPlanDTO -> {
            CurriculumPlan comboPlan = dtoConverter.convert(comboPlanDTO, CurriculumPlan.class);
            comboPlan.setCurriculum(dtoConverter.convert(comboDTO.getCurriculum(), Curriculum.class));
            comboPlan.setComboPlan(true);
            comboPlan.setCombo(dtoConverter.convert(comboDTO.getCombo(), Combo.class));
            curriculumPlanService.save(comboPlan);
        });

        // update syllabus combo of curriculum
        //noinspection OptionalGetWithoutIsPresent
        syllabusService.updateSyllabusCombo(comboDTO.getCurriculum().getId(), comboOpt.get().getCode());

        //noinspection OptionalGetWithoutIsPresent
        return get(comboDTO.getCombo().getId()).get();
    }
}