package com.unigrad.funiverseappservice.repository;

import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUserDetailRepository extends IBaseRepository<UserDetail, Long> {

    @Query(value = "select u from UserDetail u where u.curriculum = null")
    List<UserDetail> getAllUsersHaveNoCurriculum();

    @Query(value = "select ud from UserDetail ud where ud.id not in " +
            "(select gm.user.id from GroupMember gm where gm.group.id = :id)")
    List<UserDetail> getAllUsersNotInGroup(Long id);
}