package com.unigrad.funiverseappservice.repository;

import com.unigrad.funiverseappservice.entity.academic.CurriculumPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICurriculumPlanRepository extends JpaRepository<CurriculumPlan, CurriculumPlan.CurriculumPlanKey> {

    List<CurriculumPlan> getAllByCurriculum_Id(Long curriculumId);
}