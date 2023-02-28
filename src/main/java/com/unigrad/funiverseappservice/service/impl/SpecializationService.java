package com.unigrad.funiverseappservice.service.impl;

import com.unigrad.funiverseappservice.entity.academic.Specialization;
import com.unigrad.funiverseappservice.repository.ISpecializationRepository;
import com.unigrad.funiverseappservice.service.ISpecializationService;
import com.unigrad.funiverseappservice.specification.EntitySpecification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpecializationService implements ISpecializationService {

    private final ISpecializationRepository specializationRepository;

    public SpecializationService(ISpecializationRepository specializationRepository) {
        this.specializationRepository = specializationRepository;
    }

    @Override
    public List<Specialization> getAll() {
        return specializationRepository.findAll();
    }

    @Override
    public List<Specialization> getAllActive() {
        return null;
    }

    @Override
    public Optional<Specialization> get(Long key) {
        return specializationRepository.findById(key);
    }

    @Override
    public Specialization save(Specialization entity) {
        return specializationRepository.save(entity);
    }

    @Override
    public void activate(Long key) {
        specializationRepository.updateIsActive(key, true);
    }

    @Override
    public void deactivate(Long key) {
        specializationRepository.updateIsActive(key, false);
    }

    @Override
    public boolean isExist(Long key) {
        return specializationRepository.existsById(key);
    }

    @Override
    public List<Specialization> search(EntitySpecification<Specialization> specification) {
        return specializationRepository.findAll(specification);
    }
}