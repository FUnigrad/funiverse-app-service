package com.unigrad.funiverseappservice.repository;

import com.unigrad.funiverseappservice.entity.socialnetwork.GroupMember;
import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IGroupMemberRepository extends JpaRepository<GroupMember, GroupMember.GroupMemberKey> {

    List<GroupMember> findAllByGroupMemberKey_UserId(Long key);

    void deleteGroupMemberByGroupMemberKey(GroupMember.GroupMemberKey key);

    @Query(value = "select g.user from GroupMember g where g.group.id = :id")
    List<UserDetail> getAllUsersInGroup(Long id);

    @Query("select count(*) > 0 from GroupMember gm where gm.user.id = :userId and gm.group.id = :groupId and gm.isGroupAdmin = true")
    boolean isGroupAdmin(Long userId, Long groupId);

    @Query("select count(*) > 0 from GroupMember gm where gm.user.id = :userId and gm.group.id = :groupId")
    boolean isGroupMember(Long userId, Long groupId);

    int countGroupMemberByGroup_Id(Long groupId);

    @Query(value = "delete from GroupMember gm where gm.group.id = :groupId")
    @Modifying
    void deleteAllByGroupId(Long groupId);

    @Query(value = "select count (gm) > 0 from GroupMember gm where gm.user.id = :userId and gm.group.type = 'CLASS'")
    boolean isUserHaveClass(Long userId);
}