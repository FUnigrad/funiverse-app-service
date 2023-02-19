package com.unigrad.funiverseappservice.service.impl;

import com.unigrad.funiverseappservice.entity.socialnetwork.GroupMember;
import com.unigrad.funiverseappservice.repository.IGroupMemberRepository;
import com.unigrad.funiverseappservice.service.IGroupMemberService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupMemberService implements IGroupMemberService {

    @Autowired
    private IGroupMemberRepository groupMemberRepository;

    @Override
    public List<GroupMember> getAll() {
        return groupMemberRepository.findAll();
    }

    @Override
    public List<GroupMember> getAllActive() {
        return null;
    }

    @Override
    public Optional<GroupMember> get(GroupMember.GroupMemberKey key) {
        return groupMemberRepository.findById(key);
    }

    @Override
    public GroupMember save(GroupMember entity) {
        return groupMemberRepository.save(entity);
    }

    @Override
    public void activate(GroupMember.GroupMemberKey key) {

    }

    @Override
    public void deactivate(GroupMember.GroupMemberKey key) {

    }

    @Override
    public boolean isExist(GroupMember.GroupMemberKey key) {
        return groupMemberRepository.existsById(key);
    }

    @Override
    public List<GroupMember> getAllByUserId(Long key) {
        return groupMemberRepository.findAllByGroupMemberKey_UserId(key);
    }
}
