package com.unigrad.funiverseappservice.service;

import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;
import org.springframework.stereotype.Service;

@Service
public interface IAuthenCommunicateService {

    boolean saveUser(UserDetail user, String token);

    boolean inactiveUser(String email, String token);
}