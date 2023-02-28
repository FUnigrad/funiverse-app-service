package com.unigrad.funiverseappservice.service.impl;

import com.unigrad.funiverseappservice.entity.academic.Curriculum;
import com.unigrad.funiverseappservice.repository.ICurriculumRepository;
import com.unigrad.funiverseappservice.service.ICurriculumService;
import com.unigrad.funiverseappservice.specification.EntitySpecification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurriculumService implements ICurriculumService {

    private final ICurriculumRepository curriculumRepository;

    public CurriculumService(ICurriculumRepository curriculumRepository) {
        this.curriculumRepository = curriculumRepository;
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
        return curriculumRepository.save(entity);
    }

    @Override
    public void activate(Long key) {
        curriculumRepository.updateIsActive(key, true);
    }

    @Override
    public void deactivate(Long key) {
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
}