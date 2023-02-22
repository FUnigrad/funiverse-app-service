package com.unigrad.funiverseappservice.repository;

import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserDetailRepository extends IBaseRepository<UserDetail, Long> {
}
