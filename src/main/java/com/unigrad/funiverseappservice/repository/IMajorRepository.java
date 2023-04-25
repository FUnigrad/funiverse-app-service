package com.unigrad.funiverseappservice.repository;

import com.unigrad.funiverseappservice.entity.academic.Major;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IMajorRepository extends IBaseRepository<Major, Long> {

    Optional<Major> findByCode(String code);
}