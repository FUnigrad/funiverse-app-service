package com.unigrad.funiverseappservice.service;

import com.unigrad.funiverseappservice.entity.academic.Major;

import java.util.Optional;

public interface IMajorService extends IBaseService<Major, Long> {

    Optional<Major> findByCode(String code);
}