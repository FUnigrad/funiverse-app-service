package com.unigrad.funiverseappservice.service;

import com.unigrad.funiverseappservice.entity.academic.Curriculum;

public interface ICurriculumService extends IBaseService<Curriculum, Long> {

    String generateName(Curriculum curriculum);

    String generateCode(Curriculum curriculum);
}