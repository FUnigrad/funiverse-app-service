package com.unigrad.funiverseappservice.service;

public interface ISchoolYearService {

    boolean isExist(String schoolYear);

    void save(String schoolYear, Long numStudents);

    Long getNextSeq(String schoolYear);
}