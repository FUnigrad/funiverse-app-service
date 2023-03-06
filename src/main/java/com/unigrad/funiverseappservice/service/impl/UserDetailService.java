package com.unigrad.funiverseappservice.service.impl;


import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;
import com.unigrad.funiverseappservice.repository.IUserDetailRepository;
import com.unigrad.funiverseappservice.service.IUserDetailService;
import com.unigrad.funiverseappservice.specification.EntitySpecification;
import jakarta.transaction.Transactional;
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
    public List<UserDetail> getAllActive() {
        return userDetailRepository.findAllByActiveIsTrue();
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
    @Transactional
    public void activate(Long key) {
        userDetailRepository.updateIsActive(key, true);
    }

    @Override
    @Transactional
    public void inactivate(Long key) {
        userDetailRepository.updateIsActive(key, false);
    }

    @Override
    public boolean isExist(Long key) {
        return userDetailRepository.existsById(key);
    }

    @Override
    public List<UserDetail> search(EntitySpecification<UserDetail> specification) {
        return userDetailRepository.findAll(specification);
    }

    @Override
    public List<UserDetail> getAllUsersHaveNoCurriculum() {
        return userDetailRepository.getAllUsersHaveNoCurriculum();
    }
}