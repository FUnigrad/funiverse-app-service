package com.unigrad.funiverseappservice.repository;

import com.unigrad.funiverseappservice.entity.academic.Season;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ISeasonRepository extends IBaseRepository<Season, Long> {

    Optional<Season> getSeasonByOrdinalNumber(int ordinalNumber);

    Optional<Season> getSeasonByName(String name);
}