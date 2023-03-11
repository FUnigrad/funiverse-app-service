package com.unigrad.funiverseappservice.service.impl;

import com.unigrad.funiverseappservice.entity.academic.CurriculumPlan;
import com.unigrad.funiverseappservice.repository.ICurriculumPlanRepository;
import com.unigrad.funiverseappservice.service.ICurriculumPlanService;
import com.unigrad.funiverseappservice.specification.EntitySpecification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurriculumPlanService implements ICurriculumPlanService {

    private final ICurriculumPlanRepository curriculumPlanRepository;

    public CurriculumPlanService(ICurriculumPlanRepository curriculumPlanRepository) {
        this.curriculumPlanRepository = curriculumPlanRepository;
    }

    @Override
    public List<CurriculumPlan> getAll() {
        return curriculumPlanRepository.findAll();
    }

    @Override
    public List<CurriculumPlan> getAllActive() {
        return null;
    }

    @Override
    public Optional<CurriculumPlan> get(CurriculumPlan.CurriculumPlanKey key) {
        return curriculumPlanRepository.findById(key);
    }

    @Override
    public CurriculumPlan save(CurriculumPlan entity) {
        entity.setCurriculumPlanKey(new CurriculumPlan.CurriculumPlanKey(entity.getCurriculum().getId(), entity.getSyllabus().getId()));

        return curriculumPlanRepository.save(entity);
    }

    @Override
    public void activate(CurriculumPlan.CurriculumPlanKey key) {

    }

    @Override
    public void inactivate(CurriculumPlan.CurriculumPlanKey key) {

    }

    @Override
    public boolean isExist(CurriculumPlan.CurriculumPlanKey key) {
        return curriculumPlanRepository.existsById(key);
    }

    @Override
    public List<CurriculumPlan> search(EntitySpecification<CurriculumPlan> specification) {
        return null;
    }

    @Override
    public List<CurriculumPlan> getAllByCurriculumId(Long curriculumId) {
        return curriculumPlanRepository.getAllByCurriculum_Id(curriculumId);
    }

    @Override
    public List<CurriculumPlan> getAllComboPlanByCurriculumId(Long curriculumId) {
        return curriculumPlanRepository.getAllByCurriculum_IdAndComboPlan(curriculumId, true);
    }

    @Override
    public boolean removeSyllabusFromCurriculum(Long syllabusId, Long curriculumId) {
        CurriculumPlan.CurriculumPlanKey curriculumPlanKey = new CurriculumPlan.CurriculumPlanKey(curriculumId, syllabusId);

        if (curriculumPlanRepository.existsById(curriculumPlanKey)) {
            curriculumPlanRepository.deleteById(curriculumPlanKey);

            return true;
        }

        return false;
    }

    @Override
    public List<CurriculumPlan> getAllComboPlanByCurriculumIdAndComboId(Long curriculumId, Long comboId) {
        return curriculumPlanRepository.getAllByCurriculumAndCombo(curriculumId, comboId);
    }
}