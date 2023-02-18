package com.unigrad.funiverseappservice.repository;

import com.unigrad.funiverseappservice.entity.socialnetwork.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IGroupMemberRepository extends JpaRepository<GroupMember, GroupMember.GroupMemberKey> {

    List<GroupMember> findAllByGroupMemberKey_UserId(Long key);
}
