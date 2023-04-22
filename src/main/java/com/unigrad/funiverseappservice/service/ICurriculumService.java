package com.unigrad.funiverseappservice.service;

import com.unigrad.funiverseappservice.entity.academic.Curriculum;
import com.unigrad.funiverseappservice.entity.academic.Term;
import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;

import java.util.List;
import java.util.Optional;

public interface ICurriculumService extends IBaseService<Curriculum, Long> {

    String generateName(Curriculum curriculum);

    String generateCode(Curriculum curriculum);

    List<UserDetail> getUsersInCurriculum(Long id);

    List<Curriculum> getCurriculumByStartedTerm(Long id);

    List<Curriculum> getCurriculumByCurrentTerm(Long id);

    Optional<Curriculum> getCurriculumByCode(String code);
}