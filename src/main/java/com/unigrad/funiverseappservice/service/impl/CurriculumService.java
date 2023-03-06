package com.unigrad.funiverseappservice.service.impl;

import com.unigrad.funiverseappservice.entity.academic.Curriculum;
import com.unigrad.funiverseappservice.entity.academic.Major;
import com.unigrad.funiverseappservice.entity.academic.Specialization;
import com.unigrad.funiverseappservice.entity.academic.Term;
import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;
import com.unigrad.funiverseappservice.repository.ICurriculumRepository;
import com.unigrad.funiverseappservice.service.ICurriculumService;
import com.unigrad.funiverseappservice.service.IMajorService;
import com.unigrad.funiverseappservice.service.ISpecializationService;
import com.unigrad.funiverseappservice.service.ITermService;
import com.unigrad.funiverseappservice.specification.EntitySpecification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurriculumService implements ICurriculumService {

    private final ICurriculumRepository curriculumRepository;

    private final IMajorService majorService;

    private final ISpecializationService specializationService;

    private final ITermService termService;

    private final String NAME_TEMPLATE = "Bachelor Program of %s, %s Major";

    private final String CODE_TEMPLATE = "B%s_%s_%s";

    public CurriculumService(ICurriculumRepository curriculumRepository, IMajorService majorService, ISpecializationService specializationService, ITermService termService) {
        this.curriculumRepository = curriculumRepository;
        this.majorService = majorService;
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
        Optional<Term> term = termService.get(entity.getStartedTerm().getSeason(), entity.getStartedTerm().getYear());

        if (term.isPresent()) {
            entity.setStartedTerm(term.get());
        } else {
            entity.setStartedTerm(termService.save(entity.getStartedTerm()));
        }

        return curriculumRepository.save(entity);
    }

    @Override
    public void activate(Long key) {
        curriculumRepository.updateIsActive(key, true);
    }

    @Override
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
        Optional<Major> majorOpt = majorService.get(curriculum.getMajor().getId());
        Optional<Specialization> specializationOpt = specializationService.get(curriculum.getSpecialization().getId());

        if (majorOpt.isPresent() && specializationOpt.isPresent()) {

            return NAME_TEMPLATE.formatted(majorOpt.get().getName(), specializationOpt.get().getName());
        }

        return null;
    }

    @Override
    public String generateCode(Curriculum curriculum) {
        Optional<Major> majorOpt = majorService.get(curriculum.getMajor().getId());
        Optional<Specialization> specializationOpt = specializationService.get(curriculum.getSpecialization().getId());

        if (majorOpt.isPresent() && specializationOpt.isPresent()) {

            return CODE_TEMPLATE.formatted(majorOpt.get().getCode(), specializationOpt.get().getCode(), curriculum.getSchoolYear());
        }

        return null;
    }

    @Override
    public List<UserDetail> getUsersInCurriculum(Long id) {
        return curriculumRepository.getUsersInCurriculum(id);
    }
}