package com.unigrad.funiverseappservice.service;

import com.unigrad.funiverseappservice.entity.academic.CurriculumPlan;
import com.unigrad.funiverseappservice.entity.academic.Syllabus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ICurriculumPlanService extends IBaseService<CurriculumPlan, CurriculumPlan.CurriculumPlanKey> {

    List<CurriculumPlan> getAllByCurriculumId(Long curriculumId);

    List<CurriculumPlan> getAllComboPlanByCurriculumId(Long curriculumId);

    List<CurriculumPlan> getAllComboPlanByCurriculumIdAndComboId(Long curriculumId, Long comboId);

    boolean removeSyllabusFromCurriculum(Long syllabusId, Long curriculumId);

    List<Syllabus> getAllAvailableSyllabus(Long id, boolean isCombo);

    List<Syllabus> getAllSyllabusByCurriculumIdAndSemester(Long curriculumId, Integer semester);
}