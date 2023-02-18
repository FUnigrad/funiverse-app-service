package com.unigrad.funiverseappservice.repository;

import com.unigrad.funiverseappservice.entity.academic.Curriculum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICurriculumRepository extends JpaRepository<Curriculum,Long> {
}
