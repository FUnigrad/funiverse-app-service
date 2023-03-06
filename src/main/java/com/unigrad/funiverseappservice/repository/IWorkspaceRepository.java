package com.unigrad.funiverseappservice.repository;

import com.unigrad.funiverseappservice.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IWorkspaceRepository extends JpaRepository<Workspace, Long> {

}