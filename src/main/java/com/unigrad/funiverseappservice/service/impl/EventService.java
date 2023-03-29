package com.unigrad.funiverseappservice.service.impl;

import com.unigrad.funiverseappservice.entity.socialnetwork.Event;
import com.unigrad.funiverseappservice.repository.IEventRepository;
import com.unigrad.funiverseappservice.service.IEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService implements IEventService {

    private final IEventRepository eventRepository;

    @Override
    public void save(Event event) {
        eventRepository.save(event);
    }

    @Override
    public Optional<Event> get(Long id) {
        return eventRepository.findById(id);
    }

    @Override
    public List<Event> getAllForUser(Long userId) {
        return eventRepository.findAllByReceiverId(userId);
    }

    @Override
    public void read(Long id) {
        eventRepository.read(id);
    }
}