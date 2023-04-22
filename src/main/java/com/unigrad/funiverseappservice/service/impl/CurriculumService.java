package com.unigrad.funiverseappservice.service.impl;

import com.unigrad.funiverseappservice.entity.academic.Curriculum;
import com.unigrad.funiverseappservice.entity.academic.Specialization;
import com.unigrad.funiverseappservice.entity.academic.Term;
import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;
import com.unigrad.funiverseappservice.repository.ICurriculumRepository;
import com.unigrad.funiverseappservice.service.ICurriculumService;
import com.unigrad.funiverseappservice.service.ISpecializationService;
import com.unigrad.funiverseappservice.service.ITermService;
import com.unigrad.funiverseappservice.specification.EntitySpecification;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurriculumService implements ICurriculumService {

    private final ICurriculumRepository curriculumRepository;

    private final ISpecializationService specializationService;

    private final ITermService termService;

    public CurriculumService(ICurriculumRepository curriculumRepository, ISpecializationService specializationService, ITermService termService) {
        this.curriculumRepository = curriculumRepository;
        this.specializationService = specializationService;
        this.termService = termService;
    }

    @Override
    public List<Curriculum> getAll() {
        return curriculumRepository.findAll();
    }

    @Override
    public List<Curriculum> getAllActive() {
        return curriculumRepository.findAllByActiveIsTrue();
    }

    @Override
    public Optional<Curriculum> get(Long key) {
        return curriculumRepository.findById(key);
    }

    @Override
    public Curriculum save(Curriculum entity) {
        Optional<Term> term = termService.get(entity.getStartedTerm().getSeason().getId(), entity.getStartedTerm().getYear());

        entity.setStartedTerm(term.orElseGet(() -> termService.save(entity.getStartedTerm())));

        return curriculumRepository.save(entity);
    }

    @Override
    public void activate(Long key) {
        curriculumRepository.updateIsActive(key, true);
    }

    @Override
    @Transactional
    public void inactivate(Long key) {
        curriculumRepository.updateIsActive(key, false);
    }

    @Override
    public boolean isExist(Long key) {
        return curriculumRepository.existsById(key);
    }

    @Override
    public List<Curriculum> search(EntitySpecification<Curriculum> specification) {
        return curriculumRepository.findAll(specification);
    }

    @Override
    public String generateName(Curriculum curriculum) {
        String NAME_TEMPLATE = "Bachelor Program of %s, %s Major";
        Optional<Specialization> specializationOpt = specializationService.get(curriculum.getSpecialization().getId());

        return specializationOpt.map(specialization -> NAME_TEMPLATE.formatted(specialization.getMajor().getName(), specialization.getName())).orElse(null);
    }

    @Override
    public String generateCode(Curriculum curriculum) {
        String CODE_TEMPLATE = "B%s_%s_%s" + (char) (curriculum.getStartedTerm().getSeason().getOrdinalNumber() + 64);
        Optional<Specialization> specializationOpt = specializationService.get(curriculum.getSpecialization().getId());

        return specializationOpt.map(specialization ->
                CODE_TEMPLATE.formatted(specialization.getMajor().getCode(), specialization.getCode(),
                        curriculum.getSchoolYear())).orElse(null);
    }

    @Override
    public List<UserDetail> getUsersInCurriculum(Long id) {
        return curriculumRepository.getUsersInCurriculum(id);
    }

    @Override
    public List<Curriculum> getCurriculumByStartedTerm(Long id) {
        return curriculumRepository.getCurriculumByStartedTermId(id);
    }

    @Override
    public List<Curriculum> getCurriculumByCurrentTerm(Long id) {
        return curriculumRepository.getCurriculumByCurrentTermId(id);
    }

    @Override
    public Optional<Curriculum> getCurriculumByCode(String code) {
        return curriculumRepository.getCurriculumByCode(code);
    }
}