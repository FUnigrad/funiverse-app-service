package com.unigrad.funiverseappservice.service.impl;

import com.unigrad.funiverseappservice.dto.GroupMemberDTO;
import com.unigrad.funiverseappservice.entity.socialnetwork.Group;
import com.unigrad.funiverseappservice.entity.socialnetwork.GroupMember;
import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;
import com.unigrad.funiverseappservice.repository.IGroupMemberRepository;
import com.unigrad.funiverseappservice.service.IGroupMemberService;
import com.unigrad.funiverseappservice.service.IGroupService;
import com.unigrad.funiverseappservice.service.IUserDetailService;
import com.unigrad.funiverseappservice.specification.EntitySpecification;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupMemberService implements IGroupMemberService {

    private final IGroupMemberRepository groupMemberRepository;

    private final IGroupService groupService;

    private final IUserDetailService userDetailService;

    public GroupMemberService(IGroupMemberRepository groupMemberRepository, IGroupService groupService, IUserDetailService userDetailService) {
        this.groupMemberRepository = groupMemberRepository;
        this.groupService = groupService;
        this.userDetailService = userDetailService;
    }

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
    public void inactivate(GroupMember.GroupMemberKey key) {

    }

    @Override
    public boolean isExist(GroupMember.GroupMemberKey key) {
        return groupMemberRepository.existsById(key);
    }

    @Override
    public List<GroupMember> getAllByUserId(Long key) {
        return groupMemberRepository.findAllByGroupMemberKey_UserId(key);
    }

    @Transactional
    @Override
    public void deleteByGroupMemberKey(GroupMember.GroupMemberKey key) {
        groupMemberRepository.deleteGroupMemberByGroupMemberKey(key);
    }

    @Override
    public GroupMember addMemberToGroup(GroupMemberDTO groupMemberDTO) {

        Optional<Group> group = groupService.get(groupMemberDTO.getGroupId());
        Optional<UserDetail> userDetail = userDetailService.get(groupMemberDTO.getUserId());

        GroupMember groupMember = new GroupMember(new GroupMember.GroupMemberKey(groupMemberDTO.getUserId(), groupMemberDTO.getGroupId())
                , groupMemberDTO.isGroupAdmin()
                , userDetail.get()
                , group.get()
        );

        groupMemberRepository.save(groupMember);
        return groupMember;
    }

    @Override
    public List<GroupMember> search(EntitySpecification<GroupMember> specification) {
        return null;
    }
}