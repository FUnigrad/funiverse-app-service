package com.unigrad.funiverseappservice.service.impl;

import com.unigrad.funiverseappservice.entity.academic.Term;
import com.unigrad.funiverseappservice.repository.ITermRepository;
import com.unigrad.funiverseappservice.service.ITermService;
import com.unigrad.funiverseappservice.specification.EntitySpecification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TermService implements ITermService {

    private final ITermRepository termRepository;

    public TermService(ITermRepository termRepository) {
        this.termRepository = termRepository;
    }

    @Override
    public List<Term> getAll() {
        return termRepository.findAll();
    }

    @Override
    public List<Term> getAllActive() {
        return null;
    }

    @Override
    public Optional<Term> get(Long key) {
        return termRepository.findById(key);
    }

    @Override
    public Term save(Term entity) {
        return termRepository.save(entity);
    }

    @Override
    public void activate(Long key) {

    }

    @Override
    public void inactivate(Long key) {

    }

    @Override
    public boolean isExist(Long key) {
        return false;
    }

    @Override
    public List<Term> search(EntitySpecification<Term> specification) {
        return null;
    }

    @Override
    public Optional<Term> get(Long seasonId, String year) {
        return termRepository.getBySeasonIdAndYear(seasonId, year);
    }
}