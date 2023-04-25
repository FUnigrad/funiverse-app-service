package com.unigrad.funiverseappservice.repository;

import com.unigrad.funiverseappservice.entity.academic.Subject;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ISubjectRepository extends IBaseRepository<Subject, Long> {

    List<Subject> findAllByCodeLike(String code);

    Optional<Subject> findByCode(String code);
}