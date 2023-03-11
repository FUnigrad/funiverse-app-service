package com.unigrad.funiverseappservice.service;

import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IUserDetailService extends IBaseService<UserDetail, Long> {

    List<UserDetail> getAllUsersHaveNoCurriculum();

    List<UserDetail> getAllUsersNotInGroup(Long id);
}