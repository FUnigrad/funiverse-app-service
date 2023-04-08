package com.unigrad.funiverseappservice.repository;

import com.unigrad.funiverseappservice.entity.academic.CurriculumPlan;
import com.unigrad.funiverseappservice.entity.academic.Syllabus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICurriculumPlanRepository extends JpaRepository<CurriculumPlan, CurriculumPlan.CurriculumPlanKey> {

    @Query(value = "select cp from CurriculumPlan cp where cp.curriculum.id = :curriculumId and cp.isComboPlan = false")
    List<CurriculumPlan> getAllByCurriculum_Id(Long curriculumId);

    @Query(value = "select cp from CurriculumPlan cp where cp.curriculum.id = :curriculumId and cp.isComboPlan = :isComboPlan")
    List<CurriculumPlan> getAllByCurriculum_IdAndComboPlan(Long curriculumId, boolean isComboPlan);

    @Query(value = "select cp from CurriculumPlan cp where cp.curriculum.id = :curriculumId and cp.combo.id = :comboId")
    List<CurriculumPlan> getAllByCurriculumAndCombo(Long curriculumId, Long comboId);

    @Query(value = "select s from Syllabus s where s.isActive = true and s.id not in " +
            "(select cp.syllabus.id from CurriculumPlan cp where cp.curriculum.id = :curriculumId and cp.isComboPlan = false)")
    List<Syllabus> getAllSyllabusNotInCurriculum(Long curriculumId);

    @Query(value = "select s from Syllabus s where s.isActive = true and s.id not in " +
            "(select cp.syllabus.id from CurriculumPlan cp where cp.combo.id = :comboId and cp.isComboPlan = true)")
    List<Syllabus> getAllSyllabusNotInCombo(Long comboId);

    @Query(value = "select s from Syllabus s join CurriculumPlan cp on s.id = cp.syllabus.id " +
            "where cp.curriculum.id = :curriculumId and cp.semester = :semester and cp.isComboPlan = false")
    List<Syllabus> getAllSyllabusByCurriculumIdAndSemester(Long curriculumId, Integer semester);
}