package com.unigrad.funiverseappservice.service;

import com.unigrad.funiverseappservice.entity.socialnetwork.GroupMember;
import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;
import com.unigrad.funiverseappservice.payload.DTO.GroupMemberDTO;

import java.util.List;

public interface IGroupMemberService extends IBaseService<GroupMember, GroupMember.GroupMemberKey> {

    List<GroupMember> getAllByUserId(Long key);

    void deleteByGroupMemberKey(GroupMember.GroupMemberKey key);

    GroupMember addMemberToGroup(GroupMemberDTO groupMemberDTO);

    List<UserDetail> getAllUsersInGroup(Long id);

    boolean isGroupAdmin(Long userId, Long groupId);

    boolean isGroupMember(Long userId, Long groupId);

    int countMemberInGroup(Long groupId);

    void removeAllMembers(Long groupId);
}