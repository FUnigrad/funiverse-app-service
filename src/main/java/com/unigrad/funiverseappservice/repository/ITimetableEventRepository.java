package com.unigrad.funiverseappservice.repository;

import com.unigrad.funiverseappservice.entity.socialnetwork.TimetableEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ITimetableEventRepository extends JpaRepository<TimetableEvent, Long> {

    Optional<TimetableEvent> getByUserDetailIdAndSlotId(Long userId, Long slotId);
}