package com.unigrad.funiverseappservice.service.impl;


import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;
import com.unigrad.funiverseappservice.repository.IUserDetailRepository;
import com.unigrad.funiverseappservice.service.IUserDetailService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDetailService implements IUserDetailService {

    private final IUserDetailRepository userDetailRepository;

    public UserDetailService(IUserDetailRepository userDetailRepository) {
        this.userDetailRepository = userDetailRepository;
    }

    @Override
    public List<UserDetail> getAll() {
        return userDetailRepository.findAll();
    }

    @Override
    public Optional<UserDetail> get(Long key) {
        return userDetailRepository.findById(key);
    }

    @Override
    public UserDetail save(UserDetail entity) {
        return userDetailRepository.save(entity);
    }

    @Override
    public void activate(Long key) {

    }

    @Override
    public void deactivate(Long key) {

    }
}
