package com.unigrad.funiverseappservice.repository;

import com.unigrad.funiverseappservice.entity.academic.Syllabus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISyllabusRepository extends JpaRepository<Syllabus,Long> {
}
