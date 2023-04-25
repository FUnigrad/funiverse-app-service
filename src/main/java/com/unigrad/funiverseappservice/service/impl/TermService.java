package com.unigrad.funiverseappservice.service.impl;

import com.unigrad.funiverseappservice.entity.academic.Season;
import com.unigrad.funiverseappservice.entity.academic.Term;
import com.unigrad.funiverseappservice.repository.ITermRepository;
import com.unigrad.funiverseappservice.service.ISeasonService;
import com.unigrad.funiverseappservice.service.ITermService;
import com.unigrad.funiverseappservice.specification.EntitySpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TermService implements ITermService {

    private final ITermRepository termRepository;

    private final ISeasonService seasonService;

    @Override
    public List<Term> getAll() {
        return termRepository.findAll();
    }

    @Override
    public List<Term> getAllActive() {
        return null;
    }

    @Override
    public List<Term> saveAll(List<Term> entities) {
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

    @Override
    public Term getOrCreate(String term) {
        String[] termArr = term.split(" ");
        Season season = seasonService.get(termArr[0].trim()).orElseThrow(() -> new EntityNotFoundException("Season %s not valid".formatted(termArr[0])));

        Optional<Term> termOptional = get(season.getId(), termArr[1].trim());

        return termOptional.orElseGet(() -> termRepository.save(new Term(season, termArr[1].trim())));

    }
}