package com.unigrad.funiverseappservice.service.impl;

import com.unigrad.funiverseappservice.entity.academic.Syllabus;
import com.unigrad.funiverseappservice.repository.ISyllabusRepository;
import com.unigrad.funiverseappservice.service.ISyllabusService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SyllabusService implements ISyllabusService {

    private final ISyllabusRepository syllabusRepository;

    public SyllabusService(ISyllabusRepository syllabusRepository) {
        this.syllabusRepository = syllabusRepository;
    }

    @Override
    public List<Syllabus> getAll() {
        return syllabusRepository.findAll();
    }

    @Override
    public List<Syllabus> getAllActive() {
        return syllabusRepository.findAllByActiveIsTrue();
    }

    @Override
    public Optional<Syllabus> get(Long key) {
        return syllabusRepository.findById(key);
    }

    @Override
    public Syllabus save(Syllabus entity) {
        return syllabusRepository.save(entity);
    }

    @Override
    public void activate(Long key) {
        syllabusRepository.updateIsActive(key,true);
    }

    @Override
    public void deactivate(Long key) {
        syllabusRepository.updateIsActive(key,false);
    }

    @Override
    public boolean isExist(Long key) {
        return syllabusRepository.existsById(key);
    }
}