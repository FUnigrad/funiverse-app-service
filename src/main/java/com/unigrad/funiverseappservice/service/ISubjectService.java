package com.unigrad.funiverseappservice.service;

import com.unigrad.funiverseappservice.entity.academic.Subject;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ISubjectService extends IBaseService<Subject, Long> {

    List<Subject> getByCodeLike(String code);

    Optional<Subject> getByCode(String code);
}