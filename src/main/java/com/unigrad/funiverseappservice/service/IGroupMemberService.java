package com.unigrad.funiverseappservice.service;

import com.unigrad.funiverseappservice.dto.GroupMemberDTO;
import com.unigrad.funiverseappservice.entity.socialnetwork.GroupMember;

import java.util.List;

public interface IGroupMemberService extends IBaseService<GroupMember, GroupMember.GroupMemberKey> {

    List<GroupMember> getAllByUserId(Long key);

    void deleteByGroupMemberKey(GroupMember.GroupMemberKey key);

    GroupMember addMemberToGroup(GroupMemberDTO groupMemberDTO);
}