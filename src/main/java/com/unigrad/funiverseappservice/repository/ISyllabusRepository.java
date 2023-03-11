package com.unigrad.funiverseappservice.repository;

import com.unigrad.funiverseappservice.entity.academic.Syllabus;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ISyllabusRepository extends IBaseRepository<Syllabus, Long> {

    Optional<Syllabus> getByCodeEquals(String code);
}