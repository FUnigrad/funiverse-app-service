package com.unigrad.funiverseappservice.repository;

import com.unigrad.funiverseappservice.entity.socialnetwork.Group;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IGroupRepository extends IBaseRepository<Group, Long> {

    @Query(value = "select count(g) > 0 from Group g where g.type = :type and g.name = :name")
    boolean isNameExist(String name, Group.Type type);

    @Query(value = "select count(g) + 1 from Group g where g.type = 'CLASS' and g.name like :name%")
    int getNextNameOrderForClass(String name);

    @Query(value = "select g from Group g where g.type = 'CLASS' and g.curriculum.id = :curriculumId and g.isActive = true")
    List<Group> getAllClassByCurriculumId(Long curriculumId);

    Optional<Group> getBySyllabusIdAndReferenceClassId(Long syllabusId, Long referenceClassId);
}