package com.unigrad.funiverseappservice.service;

import com.unigrad.funiverseappservice.entity.academic.Season;

import java.util.Optional;

public interface ISeasonService extends IBaseService<Season, Long> {

    Season getNextSeasonOf(Season season);

    boolean isLastSeason(int ordinalNumber);

    Optional<Season> get(String season);
}