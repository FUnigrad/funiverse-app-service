package com.unigrad.funiverseappservice.service;

import com.unigrad.funiverseappservice.entity.academic.Syllabus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ISyllabusService extends IBaseService<Syllabus, Long> {

    List<Syllabus> updateSyllabusCombo(long curriculumId, String comboCode);
}