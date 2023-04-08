package com.unigrad.funiverseappservice.service.impl;

import com.unigrad.funiverseappservice.entity.socialnetwork.Group;
import com.unigrad.funiverseappservice.repository.IGroupRepository;
import com.unigrad.funiverseappservice.service.IGroupService;
import com.unigrad.funiverseappservice.specification.EntitySpecification;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GroupService implements IGroupService {

    private final IGroupRepository groupRepository;

    public GroupService(IGroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public List<Group> getAll() {
        return groupRepository.findAll();
    }

    @Override
    public List<Group> getAllActive() {
        return groupRepository.findAllByActiveIsTrue();
    }

    @Override
    public Optional<Group> get(Long key) {
        return groupRepository.findById(key);
    }

    @Override
    public Group save(Group entity) {

        LocalDateTime date = LocalDateTime.now();
        entity.setCreatedDateTime(date);

        return groupRepository.save(entity);
    }

    @Override
    @Transactional
    public void activate(Long key) {
        groupRepository.updateIsActive(key,true);
    }

    @Override
    @Transactional
    public void inactivate(Long key) {
        groupRepository.updateIsActive(key,false);
    }

    @Override
    public boolean isExist(Long key) {
        return groupRepository.existsById(key);
    }

    @Override
    public List<Group> search(EntitySpecification<Group> specification) {
        return groupRepository.findAll(specification);
    }

    @Override
    public boolean isNameExist(String name, Group.Type type) {
        return groupRepository.isNameExist(name, type);
    }

    @Override
    public int getNextNameOrderForClass(String name) {
        return groupRepository.getNextNameOrderForClass(name);
    }

    @Override
    public List<Group> getAllClassByCurriculumId(Long curriculumId) {
        return groupRepository.getAllClassByCurriculumId(curriculumId);
    }

    @Override
    public Optional<Group> getBySyllabusIdAndReferenceClassId(Long syllabusId, Long referenceClassId) {
        return groupRepository.getBySyllabusIdAndReferenceClassId(syllabusId, referenceClassId);
    }
}