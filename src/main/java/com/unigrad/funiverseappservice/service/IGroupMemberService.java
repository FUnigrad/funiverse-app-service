package com.unigrad.funiverseappservice.service;

import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;
import com.unigrad.funiverseappservice.payload.GroupMemberDTO;
import com.unigrad.funiverseappservice.entity.socialnetwork.GroupMember;

import java.util.List;

public interface IGroupMemberService extends IBaseService<GroupMember, GroupMember.GroupMemberKey> {

    List<GroupMember> getAllByUserId(Long key);

    void deleteByGroupMemberKey(GroupMember.GroupMemberKey key);

    GroupMember addMemberToGroup(GroupMemberDTO groupMemberDTO);

    List<UserDetail> getAllUsersInGroup(Long id);

    List<UserDetail> getAllUsersNotInGroup(Long id);
}