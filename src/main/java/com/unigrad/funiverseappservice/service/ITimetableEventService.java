package com.unigrad.funiverseappservice.service;

import com.unigrad.funiverseappservice.entity.socialnetwork.TimetableEvent;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface ITimetableEventService {

    Optional<TimetableEvent> getByUserIdAndSlotId(Long userId, Long slotId);

    TimetableEvent save(TimetableEvent timetableEvent);
}