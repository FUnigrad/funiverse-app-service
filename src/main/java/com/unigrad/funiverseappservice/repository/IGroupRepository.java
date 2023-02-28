package com.unigrad.funiverseappservice.repository;

import com.unigrad.funiverseappservice.entity.socialnetwork.Group;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IGroupRepository extends IBaseRepository<Group, Long> {

}