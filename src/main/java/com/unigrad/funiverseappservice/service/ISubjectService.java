package com.unigrad.funiverseappservice.service;

import com.unigrad.funiverseappservice.entity.academic.Subject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ISubjectService extends IService<Subject, Long> {

    List<Subject> getByCode(String code);
}