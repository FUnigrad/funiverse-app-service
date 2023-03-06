package com.unigrad.funiverseappservice.repository;

import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUserDetailRepository extends IBaseRepository<UserDetail, Long> {

    @Query(value = "select u from UserDetail u where u.curriculum = null")
    List<UserDetail> getAllUsersHaveNoCurriculum();
}