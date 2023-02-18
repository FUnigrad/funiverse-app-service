package com.unigrad.funiverseappservice.service.impl;

import com.unigrad.funiverseappservice.entity.academic.Curriculum;
import com.unigrad.funiverseappservice.repository.ICurriculumRepository;
import com.unigrad.funiverseappservice.service.ICurriculumService;
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
    public Optional<Curriculum> get(Long key) {
        return curriculumRepository.findById(key);
    }

    @Override
    public Curriculum save(Curriculum entity) {
        return curriculumRepository.save(entity);
    }

    @Override
    public void activate(Long key) {

    }

    @Override
    public void deactivate(Long key) {

    }
}
