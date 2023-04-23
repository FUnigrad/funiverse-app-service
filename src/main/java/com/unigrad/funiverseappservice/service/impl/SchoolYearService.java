package com.unigrad.funiverseappservice.service.impl;

import com.unigrad.funiverseappservice.entity.academic.SchoolYear;
import com.unigrad.funiverseappservice.repository.ISchoolYearRepository;
import com.unigrad.funiverseappservice.service.ISchoolYearService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SchoolYearService implements ISchoolYearService {

    private final ISchoolYearRepository schoolYearRepository;

    @Override
    public boolean isExist(String schoolYear) {
        return schoolYearRepository.existsById(schoolYear);
    }

    @Override
    public void save(String schoolYear, Long numStudents) {
        schoolYearRepository.save(new SchoolYear(schoolYear, numStudents));
    }

    @Override
    public Long getNextSeq(String schoolYear) {
        Optional<SchoolYear> schoolYearOptional = schoolYearRepository.findById(schoolYear);

        if (schoolYearOptional.isEmpty()) {
            return 0L;
        }

        Long nextSeq = schoolYearOptional.get().getNextStudentSeq();
        schoolYearOptional.get().setNextStudentSeq(nextSeq + 1);
        schoolYearRepository.save(schoolYearOptional.get());

        return nextSeq;
    }
}