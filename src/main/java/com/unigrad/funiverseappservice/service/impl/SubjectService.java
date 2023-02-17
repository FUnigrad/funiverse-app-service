package com.unigrad.funiverseappservice.service.impl;

import com.unigrad.funiverseappservice.entity.academic.Subject;
import com.unigrad.funiverseappservice.repository.ISubjectRepository;
import com.unigrad.funiverseappservice.service.ISubjectService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectService implements ISubjectService {

    private final ISubjectRepository subjectRepository;

    public SubjectService(ISubjectRepository iSubjectRepository) {
        this.subjectRepository = iSubjectRepository;
    }

    @Override
    public List<Subject> getAll() {
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
    public void activate(Long key) {
        subjectRepository.updateIsActive(key, true);
    }

    @Override
    public void deactivate(Long key) {
        subjectRepository.updateIsActive(key, false);
    }

}