package com.unigrad.funiverseappservice.repository;

import com.unigrad.funiverseappservice.entity.academic.Specialization;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ISpecializationRepository extends IBaseRepository<Specialization, Long>{

    Optional<Specialization> findByCode(String code);
}