package com.unigrad.funiverseappservice.repository;

import com.unigrad.funiverseappservice.entity.academic.Combo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IComboRepository extends IBaseRepository<Combo, Long> {

    @Query(value = "select c from Combo c join CurriculumPlan cp on c.id = cp.combo.id" +
            " where cp.curriculum.id = :id")
    List<Combo> getAllByCurriculumId(Long id);
}