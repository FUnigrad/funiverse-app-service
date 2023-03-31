package com.unigrad.funiverseappservice.repository;

import com.unigrad.funiverseappservice.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IWorkspaceRepository extends JpaRepository<Workspace, Long> {

    @Query(value = "select w.slotDurationInMin from Workspace w")
    Integer getSlotDurationInMin();

    @Query(value = "select w.restTimeInMin from Workspace w")
    Integer getRestTimeInMin();
}