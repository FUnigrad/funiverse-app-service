package com.unigrad.funiverseappservice.repository;

import com.unigrad.funiverseappservice.entity.academic.Subject;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISubjectRepository extends IBaseRepository<Subject, Long> {

    List<Subject> findAllByCodeLike(String code);
}