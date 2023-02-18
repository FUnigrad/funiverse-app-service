package com.unigrad.funiverseappservice.service.impl;

import com.unigrad.funiverseappservice.entity.academic.Syllabus;
import com.unigrad.funiverseappservice.repository.ISyllabusRepository;
import com.unigrad.funiverseappservice.service.ISyllabusService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Optional<Syllabus> get(Long key) {
        return syllabusRepository.findById(key);
    }

    @Override
    public Syllabus save(Syllabus entity) {
        return syllabusRepository.save(entity);
    }

    @Override
    public void activate(Long key) {

    }

    @Override
    public void deactivate(Long key) {

    }
}
