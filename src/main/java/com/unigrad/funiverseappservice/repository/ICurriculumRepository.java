package com.unigrad.funiverseappservice.repository;

import com.unigrad.funiverseappservice.entity.academic.Curriculum;
import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ICurriculumRepository extends IBaseRepository<Curriculum, Long> {

    @Query(value = "select c.users from Curriculum c where c.id = :id")
    List<UserDetail> getUsersInCurriculum(Long id);

    List<Curriculum> getCurriculumByStartedTermId(Long id);

    List<Curriculum> getCurriculumByCurrentTermId(Long id);
}