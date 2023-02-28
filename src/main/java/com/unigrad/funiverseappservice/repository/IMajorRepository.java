package com.unigrad.funiverseappservice.repository;

import com.unigrad.funiverseappservice.entity.academic.Major;
import org.springframework.stereotype.Repository;

@Repository
public interface IMajorRepository extends IBaseRepository<Major, Long> {
}