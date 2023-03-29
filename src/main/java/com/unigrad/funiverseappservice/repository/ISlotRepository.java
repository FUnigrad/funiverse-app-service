package com.unigrad.funiverseappservice.repository;

import com.unigrad.funiverseappservice.entity.academic.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISlotRepository extends JpaRepository<Slot, Long> {
}