package com.unigrad.funiverseappservice.service.impl;

import com.unigrad.funiverseappservice.entity.socialnetwork.Group;
import com.unigrad.funiverseappservice.repository.IGroupRepository;
import com.unigrad.funiverseappservice.service.IGroupService;
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
    public void activate(Long key) {
        groupRepository.updateIsActive(key,true);
    }

    @Override
    public void deactivate(Long key) {
        groupRepository.updateIsActive(key,false);
    }

    @Override
    public boolean isExist(Long key) {
        return groupRepository.existsById(key);
    }

}
