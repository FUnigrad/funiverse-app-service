package com.unigrad.funiverseappservice.repository;

import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserDetailRepository extends JpaRepository<UserDetail,Long> {
}
