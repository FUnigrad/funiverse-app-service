package com.unigrad.funiverseappservice.service.impl;

import com.unigrad.funiverseappservice.entity.academic.Major;
import com.unigrad.funiverseappservice.repository.IMajorRepository;
import com.unigrad.funiverseappservice.service.IMajorService;
import com.unigrad.funiverseappservice.specification.EntitySpecification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MajorService implements IMajorService {

    private final IMajorRepository majorRepository;

    public MajorService(IMajorRepository majorRepository) {
        this.majorRepository = majorRepository;
    }

    @Override
    public List<Major> getAll() {
        return majorRepository.findAll();
    }

    @Override
    public List<Major> getAllActive() {
        return null;
    }

    @Override
    public Optional<Major> get(Long key) {
        return majorRepository.findById(key);
    }

    @Override
    public Major save(Major entity) {
        return majorRepository.save(entity);
    }

    @Override
    public void activate(Long key) {
        majorRepository.updateIsActive(key, true);
    }

    @Override
    public void deactivate(Long key) {
        majorRepository.updateIsActive(key, false);
    }

    @Override
    public boolean isExist(Long key) {
        return majorRepository.existsById(key);
    }

    @Override
    public List<Major> search(EntitySpecification<Major> specification) {
        return majorRepository.findAll(specification);
    }
}