package com.unigrad.funiverseappservice.service;

import com.unigrad.funiverseappservice.entity.academic.Specialization;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface ISpecializationService extends IBaseService<Specialization, Long> {

    Optional<Specialization> getByCode(String code);
}