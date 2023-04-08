package com.unigrad.funiverseappservice.service.impl;

import com.unigrad.funiverseappservice.entity.socialnetwork.TimetableEvent;
import com.unigrad.funiverseappservice.repository.ITimetableEventRepository;
import com.unigrad.funiverseappservice.service.ITimetableEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TimetableEventService implements ITimetableEventService {

    private final ITimetableEventRepository timetableEventRepository;

    @Override
    public Optional<TimetableEvent> getByUserIdAndSlotId(Long userId, Long slotId) {
        return timetableEventRepository.getByUserDetailIdAndSlotId(userId, slotId);
    }

    @Override
    public TimetableEvent save(TimetableEvent timetableEvent) {
        return timetableEventRepository.save(timetableEvent);
    }
}