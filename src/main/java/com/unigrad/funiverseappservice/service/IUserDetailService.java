package com.unigrad.funiverseappservice.service;

import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface IUserDetailService extends IBaseService<UserDetail, Long> {

    List<UserDetail> getAllUsersHaveNoCurriculum();

    List<UserDetail> getAllUsersNotInGroup(Long id);

    Optional<UserDetail> findByPersonalMail(String eduMail);
}