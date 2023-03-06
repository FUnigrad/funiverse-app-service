package com.unigrad.funiverseappservice.service;

import com.unigrad.funiverseappservice.entity.academic.Curriculum;
import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;

import java.util.List;

public interface ICurriculumService extends IBaseService<Curriculum, Long> {

    String generateName(Curriculum curriculum);

    String generateCode(Curriculum curriculum);

    List<UserDetail> getUsersInCurriculum(Long id);
}