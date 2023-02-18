package com.unigrad.funiverseappservice.service;

import com.unigrad.funiverseappservice.entity.socialnetwork.GroupMember;

import java.util.List;

public interface IGroupMemberService extends IService<GroupMember, GroupMember.GroupMemberKey> {

    List<GroupMember> getAllByUserId(Long key);
}
