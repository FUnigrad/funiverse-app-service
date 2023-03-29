package com.unigrad.funiverseappservice.service.impl;

import com.unigrad.funiverseappservice.entity.academic.Season;
import com.unigrad.funiverseappservice.repository.ISeasonRepository;
import com.unigrad.funiverseappservice.service.ISeasonService;
import com.unigrad.funiverseappservice.specification.EntitySpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SeasonService implements ISeasonService {

    private final ISeasonRepository seasonRepository;

    @Override
    public List<Season> getAll() {
        return seasonRepository.findAll();
    }

    @Override
    public List<Season> getAllActive() {
        return seasonRepository.findAllByActiveIsTrue();
    }

    @Override
    public Optional<Season> get(Long key) {
        return seasonRepository.findById(key);
    }

    @Override
    public Season save(Season entity) {
        return seasonRepository.save(entity);
    }

    @Override
    public void activate(Long key) {
        seasonRepository.updateIsActive(key, true);
    }

    @Override
    public void inactivate(Long key) {
        seasonRepository.updateIsActive(key, false);
    }

    @Override
    public boolean isExist(Long key) {
        return seasonRepository.existsById(key);
    }

    @Override
    public List<Season> search(EntitySpecification<Season> specification) {
        return null;
    }

    @Override
    public Season getNextSeasonOf(Season season) {
        List<Season> seasons = getAllActive();

        //noinspection OptionalGetWithoutIsPresent
        return season.getOrdinalNumber() == seasons.size()
                ? seasons.get(0)
                : seasonRepository.getSeasonByOrdinalNumber(season.getOrdinalNumber() + 1).get();

    }

    @Override
    public boolean isLastSeason(int ordinalNumber) {
        List<Season> seasons = getAllActive();
        return ordinalNumber == seasons.size();
    }
}