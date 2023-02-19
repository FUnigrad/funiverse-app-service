package com.unigrad.funiverseappservice.repository;

import com.unigrad.funiverseappservice.entity.socialnetwork.Group;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IGroupRepository extends IBaseRepository<Group, Long> {

    @Query(value = "SELECT g FROM Group g WHERE g.name like %:name% ", nativeQuery = true)
    List<Group> findAllByNameLike(@Param("name") String name);
}
