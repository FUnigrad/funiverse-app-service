package com.unigrad.funiverseappservice.service;

import com.unigrad.funiverseappservice.entity.socialnetwork.Group;

import java.util.List;
import java.util.Optional;

public interface IGroupService extends IBaseService<Group,Long> {

    boolean isNameExist(String name, Group.Type type);

    int getNextNameOrderForClass(String name);

    List<Group> getAllClassByCurriculumId(Long curriculumId);

    Optional<Group> getBySyllabusIdAndReferenceClassId(Long syllabusId, Long referenceClassId);

    Group getClassByStudentId(Long studentId);

    List<Group> getAllForUser(Long userId, String groupName);

    List<Group> getTeachingClass(Long teacherId);
}