package com.unigrad.funiverseappservice.repository;

import com.unigrad.funiverseappservice.entity.socialnetwork.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IGroupRepository extends JpaRepository<Group,Long> {

}
