package com.unigrad.funiverseappservice.repository;

import com.unigrad.funiverseappservice.entity.academic.SchoolYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISchoolYearRepository extends JpaRepository<SchoolYear, String> {

}