package com.unigrad.funiverseappservice.repository;

import com.unigrad.funiverseappservice.entity.academic.Syllabus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ISyllabusRepository extends IBaseRepository<Syllabus, Long> {

    Optional<Syllabus> getByCodeEquals(String code);

    @Query("select s from Syllabus s right outer join CurriculumPlan cp on s.id = cp.syllabus.id " +
            "where s.isSyllabusCombo = false and cp.curriculum.id != :curriculumId")
    List<Syllabus> getReadySyllabusForAdding(Long curriculumId);

    Optional<Syllabus> findByCode(String code);
}