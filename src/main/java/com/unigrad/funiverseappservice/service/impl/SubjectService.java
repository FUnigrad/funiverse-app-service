package com.unigrad.funiverseappservice.service.impl;

import com.unigrad.funiverseappservice.entity.academic.Subject;
import com.unigrad.funiverseappservice.repository.ISubjectRepository;
import com.unigrad.funiverseappservice.service.ISubjectService;
import com.unigrad.funiverseappservice.specification.EntitySpecification;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectService implements ISubjectService{

    private final ISubjectRepository subjectRepository;

    public SubjectService(ISubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Override
    public List<Subject> getAll() {
        return subjectRepository.findAll();
    }

    @Override
    public List<Subject> getAllActive() {
        return subjectRepository.findAllByActiveIsTrue();
    }

    @Override
    public Optional<Subject> get(Long key) {
        return subjectRepository.findById(key);
    }

    public List<Subject> getByCode(String code) {
        return subjectRepository.findAllByCodeLike(code);
    }

    @Override
    public Subject save(Subject entity) {
        return subjectRepository.save(entity);
    }

    @Override
    @Transactional
    public void activate(Long key) {
        subjectRepository.updateIsActive(key, true);
    }

    @Transactional
    @Override
    public void inactivate(Long key) {
        subjectRepository.updateIsActive(key, false);
    }

    @Override
    public boolean isExist(Long key) {
        return subjectRepository.existsById(key);
    }

    @Override
    public List<Subject> search(EntitySpecification<Subject> specification) {
        return subjectRepository.findAll(specification);
    }
}